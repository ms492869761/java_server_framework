package com.game.core.db.mongo;

import java.util.HashMap;

/**
 * mongoDB访问管理
 * @author wangzhiyuan
 *
 */
public class MongoDbManager {
	
	private static MongoDbManager instance=new MongoDbManager();
	
	private MongoDbManager() {
		
	}
	public static MongoDbManager getInstance() {
		return instance;
	}
	
	/** mongoDB dao集合*/
	private HashMap<String, MongoDAO> clientMap=new HashMap<>();
	
	/**
	 * 创建mongoDBDAO
	 * @param plat
	 * @param mongoDbConfig
	 * @throws Exception
	 */
	public void createMongoDao(String plat,MongoDbConfig mongoDbConfig) throws Exception {
		if(clientMap.containsKey(plat)) {
			throw new Exception("mongo db plat already exists");
		}
		MongoDAO mongoDAO=new MongoDAO(mongoDbConfig);
		clientMap.put(plat, mongoDAO);
	}
	
	/**
	 * 重载mongoDbDao
	 * @param plat
	 * @param mongoDbConfig
	 * @throws Exception
	 */
	public void resetMongoDao(String plat,MongoDbConfig mongoDbConfig) throws Exception {
		if(!clientMap.containsKey(plat)) {
			throw new Exception("mongo db plat don't exists");
		}
		MongoDAO mongoDAO = clientMap.get(plat);
		mongoDAO.shutdown();
		clientMap.put(plat, new MongoDAO(mongoDbConfig));
	}
	
	/**
	 * 获取mongoDbDao
	 * @param plat
	 * @return
	 */
	public MongoDAO getMongoDaoByPlat(String plat) {
		if(!clientMap.containsKey(plat)) {
			return null;
		}
		return clientMap.get(plat);
	}
	
	/**
	 * 关闭所有数据库DAO
	 */
	public void shutdown() {
		clientMap.values().stream().forEach(dao->{
			dao.shutdown();
		});
		clientMap.clear();
	}
	
}
