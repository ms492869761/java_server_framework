package com.game.core.db.mongospring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;

public class TestMongo {
	
	public static void main(String[] args) {
		MongoConfig mongoConfig=new MongoConfig();
		mongoConfig.setAuthDBName("admin");
		mongoConfig.setDbName("xianpro_dev10");
		mongoConfig.setConnPreHost(5);
		mongoConfig.setUsername("siteRootAdmin");
		mongoConfig.setPassword("mongodb_password");
		mongoConfig.setReplica("");
		mongoConfig.setThreadsAllowedToBlockForConnectionMultiplier(5);
		List<BindConfig> adds=new ArrayList<>();
		BindConfig bindConfig=new BindConfig();
		bindConfig.setHost("172.16.170.143");
		bindConfig.setPort(31000);
		adds.add(bindConfig);
		mongoConfig.setCluster(adds);
		Map<String, MongoConfig> mongoConfigMap=new HashMap<>();
		mongoConfigMap.put("default", mongoConfig);
		MongoFactory mongoFactory=new MongoFactory();
		mongoFactory.init(mongoConfigMap);
		BaseRepository daoByKey = mongoFactory.getDaoByKey("default");
		System.out.println(JSON.toJSONString(mongoConfig));
		HashMap<String, String> dataMap=new HashMap<>();
		Set<String> fields=new HashSet<>();
		fields.add("partners");
		HashMap<String, String> docById = daoByKey.getDocById("dev10_226",  dataMap.getClass(), "users", fields);
		
		
		System.out.println(docById);
	}
}


class TestMongoDbBean {
	
	private String id;
	
	private String name;
	
	private int age;
	
	private String address;

	private List<String> nicknames=new ArrayList<>();
	
	private Map<String, Object> attributes=new HashMap<>();
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public List<String> getNicknames() {
		return nicknames;
	}
	
	public void setNicknames(List<String> nicknames) {
		this.nicknames = nicknames;
	}
	
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
}
