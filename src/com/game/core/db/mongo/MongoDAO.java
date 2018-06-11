package com.game.core.db.mongo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.game.core.common.utils.UUIDService;
import com.game.core.db.mongo.ann.MongoCollectionAnn;
import com.game.core.db.mongo.bean.BaseMongoPersistenceBean;
import com.game.core.db.mongo.bean.MongoAddressBean;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class MongoDAO {
	
	private MongoClient client;
	
	private MongoDbConfig config;
	
	public MongoDAO(MongoDbConfig config) {
		this.config=config;
		// 构建mongo连接属性
		Builder builder = MongoClientOptions.builder();
		builder.readPreference(ReadPreference.primaryPreferred());
		builder.connectionsPerHost(config.getConnectPreHost());
		builder.minConnectionsPerHost(config.getConnectPreHost());
		builder.cursorFinalizerEnabled(true);
		builder.socketTimeout(config.getSocketTimeout());
		builder.threadsAllowedToBlockForConnectionMultiplier(config.getThreadsAllowedToBlockForConnectionMultiplier());
		// 构建mongodb 登录权限
		MongoCredential createCredential = MongoCredential.createCredential(config.getUser(), config.getAuthDataBase()	, config.getPassword().toCharArray());
		
		List<MongoAddressBean> addressList = config.getAddressList();
		if(addressList.size()>1) {
			builder.requiredReplicaSetName(config.getReplicaSet());
			MongoClientOptions options = builder.build();
			List<ServerAddress> addrs=new ArrayList<>();
			for (MongoAddressBean mongoAddressBean : addressList) {
				ServerAddress serverAddress = new ServerAddress(mongoAddressBean.getHost(), mongoAddressBean.getPort());
				addrs.add(serverAddress);
			}
			client=new MongoClient(addrs,createCredential,options);
		} else {
			MongoClientOptions options = builder.build();
			MongoAddressBean mongoAddressBean = addressList.get(0);
			ServerAddress addr=new ServerAddress(mongoAddressBean.getHost(),mongoAddressBean.getPort());
			client=new MongoClient(addr,createCredential,options);
		}
	}
	
	
	public <T extends BaseMongoPersistenceBean> void save(T doc) throws Exception {
		// 获取collection注解
		MongoCollectionAnn annotation = doc.getClass().getAnnotation(MongoCollectionAnn.class);
		if(annotation==null) {
			throw new Exception("error mongo persistence bean don't have MongoCollection Annotation");
		}
		String collection = annotation.Collection();
		save(doc, collection);
	}

	
	public <T> void save(T doc,String collection) throws Exception{
		// 判断持久化对象是否为空 
		if(doc==null) {
			throw new Exception("error mongo persistence bean is null");
		}
		// 验证collection
		if(StringUtils.isEmpty(collection)) {
			throw new Exception("error mongo collection is empty");
		}
		
		MongoDatabase database = client.getDatabase(config.getUserDataBase());
		MongoCollection<Document> mongoCollection = database.getCollection(collection);
		String jsonObject = JSON.toJSONString(doc);
		Document document = Document.parse(jsonObject);
		long count = mongoCollection.count(document);
		if(count>0) {
			throw new Exception("error the primary key already exists");
		}
		mongoCollection.insertOne(document);
	}
	
	/**
	 * 修改一条记录
	 * @param doc
	 * @throws Exception
	 */
	public <T extends BaseMongoPersistenceBean> void update(T doc) throws Exception {
		MongoCollectionAnn annotation = doc.getClass().getAnnotation(MongoCollectionAnn.class);
		if(annotation==null) {
			throw new Exception("error mongo persistence Annotation is null");
		}
		String collection = annotation.Collection();
		update(doc, collection);
	}
	
	/**
	 * 修改一条记录
	 * @param doc
	 * @param collection
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseMongoPersistenceBean> boolean update(T doc,String collection) throws Exception {
		// 判断持久化对象是否为空 
		if(doc==null) {
			throw new Exception("error mongo persistence bean is null");
		}
		// 验证collection
		if(StringUtils.isEmpty(collection)) {
			throw new Exception("error mongo collection is empty");
		}
		MongoDatabase database = client.getDatabase(config.getUserDataBase());
		MongoCollection<Document> mongoCollection = database.getCollection(collection);
		BasicDBObject query=new BasicDBObject("_id", doc.getMongoId());
		String jsonObject = JSON.toJSONString(doc);
		Document document = Document.parse(jsonObject);
		long count = mongoCollection.count(query);
		
		if(count<=0) {
			throw new Exception("error the primary key not exists");
		}
		UpdateResult updateOne = mongoCollection.replaceOne(query, document);
		long modifiedCount = updateOne.getModifiedCount();
		return modifiedCount>=1;
	}
	
	/**
	 * 通过ID查找
	 * @param id
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseMongoPersistenceBean> T queryById(String id,Class<T> cls) throws Exception{
		MongoCollectionAnn annotation = cls.getAnnotation(MongoCollectionAnn.class);
		if(annotation==null) {
			throw new Exception("error mongo persistence Annotation is null");
		}
		MongoDatabase database = client.getDatabase(config.getUserDataBase());
		MongoCollection<Document> mongoCollection = database.getCollection(annotation.Collection());
		BasicDBObject query=new BasicDBObject("_id",id);
		long count = mongoCollection.count(query);
		if(count<=0) {
			return null;
		}
		FindIterable<Document> find = mongoCollection.find(query);
		MongoCursor<Document> iterator = find.iterator();
		while(iterator.hasNext()) {
			Document next = iterator.next();
			String json = next.toJson();
			T javaObject = JSON.toJavaObject(JSON.parseObject(json), cls);
			return javaObject;
		}
		return null;
	}
	
	/**
	 * 删除对象
	 * @param id
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseMongoPersistenceBean> boolean delete(String id,Class<T> cls) throws Exception{
		MongoCollectionAnn annotation = cls.getAnnotation(MongoCollectionAnn.class);
		if(annotation==null) {
			throw new Exception("error mongo persistence Annotation is null");
		}
		MongoDatabase database = client.getDatabase(config.getUserDataBase());
		MongoCollection<Document> mongoCollection = database.getCollection(annotation.Collection());
		BasicDBObject query=new BasicDBObject("_id", id);
		DeleteResult deleteOne = mongoCollection.deleteOne(query);
		long deletedCount = deleteOne.getDeletedCount();
		if(deletedCount>=1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 删除对象
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseMongoPersistenceBean> boolean delete(T doc) throws Exception{
		if(doc==null) {
			throw new Exception("error mongo persistence bean is null");
		}
		String mongoId = doc.getMongoId();
		return delete(mongoId, doc.getClass());
	}
	
	
	
	/**
	 * 关闭链接
	 */
	public void shutdown() {
		client.close();
	}
	
	
	/**
	 * 测试用例
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
			MongoDbConfig config=new MongoDbConfig();
			config.setAuthDataBase("admin");
			config.setUserDataBase("test");
			MongoAddressBean mongoAddressBean = new MongoAddressBean();
			mongoAddressBean.setHost("172.16.170.143");
			mongoAddressBean.setPort(31000);
			MongoAddressBean mongoAddressBean2 = new MongoAddressBean();
			mongoAddressBean2.setHost("172.16.170.143");
			mongoAddressBean2.setPort(32000);
			MongoAddressBean mongoAddressBean3 = new MongoAddressBean();
			mongoAddressBean3.setHost("172.16.170.143");
			mongoAddressBean3.setPort(33000);
			config.getAddressList().add(mongoAddressBean);
			config.getAddressList().add(mongoAddressBean2);
			config.getAddressList().add(mongoAddressBean3);
			config.setConnectPreHost(5);
			config.setId("test");
			config.setPassword("mongodb_password");
			config.setUser("siteRootAdmin");
			config.setSocketTimeout(30);
			config.setThreadsAllowedToBlockForConnectionMultiplier(5);
			config.setReplicaSet("replset_1");
			TestJson tj=new TestJson();
			tj.setMongoId(UUIDService.getInstance().getId(1)+"");
			tj.setName("yuanzhiwang");
			MongoDAO mongoDAO = new MongoDAO(config);
			mongoDAO.update(tj);
			TestJson queryById = mongoDAO.queryById("100183695360001", TestJson.class);
			System.out.println(JSON.toJSON(queryById));
			
	}
	
	
	
}

/**
 * 测试mongoDb持久化对象
 * @author wangzhiyuan
 *
 */
@MongoCollectionAnn(Collection="testJavaMongodb")
class TestJson extends BaseMongoPersistenceBean{
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

