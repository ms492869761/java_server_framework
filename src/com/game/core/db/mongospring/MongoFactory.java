package com.game.core.db.mongospring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.game.core.common.logger.LoggerExecuteHandler;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

public class MongoFactory {
	
	
	private Map<String, BaseRepository> mongoDbDaoMap=new HashMap<>();
	
	public void init(Map<String, MongoConfig> mongoConfigMap) {
		Set<Entry<String,MongoConfig>> entrySet = mongoConfigMap.entrySet();
		entrySet.stream().forEach(entry->{
			String key = entry.getKey();
			MongoConfig value = entry.getValue();
			LoggerExecuteHandler.getInstance().dealInfoLogger("init mongo config , key:{} ,value:{}", key,value);
			MongoTemplate mongoTemplate = getMongoTemplate(value);
			BaseRepository baseRepository = new BaseRepository(mongoTemplate);
			this.mongoDbDaoMap.put(key, baseRepository);
		});
	}
	
	private MongoTemplate getMongoTemplate(MongoConfig mongoConfig) {
		MongoClient mongoClient=null;
		Builder builder = MongoClientOptions.builder();
		builder.readPreference(ReadPreference.primaryPreferred());
		builder.minConnectionsPerHost(mongoConfig.getConnPreHost());
		builder.cursorFinalizerEnabled(true);
		builder.socketTimeout(mongoConfig.getSocketTimeout());
		builder.threadsAllowedToBlockForConnectionMultiplier(mongoConfig.getThreadsAllowedToBlockForConnectionMultiplier());
		try {
			if(StringUtils.isBlank(mongoConfig.getReplica())) {
				MongoClientOptions build = builder.build();
				String host = mongoConfig.getCluster().get(0).getHost();
				int port = mongoConfig.getCluster().get(0).getPort();
				ServerAddress serverAddress = new ServerAddress(host,port);
				if(StringUtils.isBlank(mongoConfig.getPassword())) {
					mongoClient=new MongoClient(serverAddress, build);
				} else {
					MongoCredential createCredential = MongoCredential.createCredential(mongoConfig.getUsername(), mongoConfig.getAuthDBName(), mongoConfig.getPassword().toCharArray());
					mongoClient=new MongoClient(serverAddress,createCredential,build);
				}
			} else {
				builder.requiredReplicaSetName(mongoConfig.getReplica());
				MongoClientOptions build = builder.build();
				List<ServerAddress> addrs=new ArrayList<>();
				mongoConfig.getCluster().stream().forEach(cluster->{
					String host = cluster.getHost();
					int port = cluster.getPort();
					ServerAddress serverAddress = new ServerAddress(host, port);
					addrs.add(serverAddress);
				});
				if(StringUtils.isBlank(mongoConfig.getPassword())) {
					mongoClient=new MongoClient(addrs,build);
				} else {
					MongoCredential createCredential = MongoCredential.createCredential(mongoConfig.getUsername(), mongoConfig.getAuthDBName(), mongoConfig.getPassword().toCharArray());
					mongoClient=new MongoClient(addrs,createCredential,build);
				}
			}
		} catch (Exception e) {
			LoggerExecuteHandler.getInstance().dealExceptionLogger("mongoDB INIT faild", e);
			System.exit(0);
		}
		return new MongoTemplate(mongoClient, mongoConfig.getDbName());
	}
	
	public Map<String, BaseRepository> getAllMongoDbDao() {
		return this.mongoDbDaoMap;
	}
	
	public BaseRepository getDaoByKey(String key) {
		return this.mongoDbDaoMap.get(key);
	}
	
	
	
	
	
	
	
	
	
	
	
}
