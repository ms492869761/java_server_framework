package com.game.core.db.mongo;

import java.util.HashMap;

import com.mongodb.MongoClient;

public class MongoDbUtil {
	
	private static MongoDbUtil instance=new MongoDbUtil();
	
	private MongoDbUtil() {
		
	}
	public static MongoDbUtil getInstance() {
		return instance;
	}
	
	private HashMap<String, MongoClient> clientMap=new HashMap<>();
	
	
	
	
	
}
