package com.game.core.db.mongospring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMongo {
	
	public static void main(String[] args) {
			
		
		MongoConfig mongoConfig=new MongoConfig();
		mongoConfig.setAuthDBName("admin");
		mongoConfig.setDbName("test");
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
		
		
		
		TestMongoDbBean bean=new TestMongoDbBean();
		bean.setId("123456789");
		bean.setName("wangzhiyuan");
		bean.setAge(100);
		bean.setAddress("beijing");
		bean.getNicknames().add("123");
		bean.getNicknames().add("456");
		bean.getNicknames().add("789");
		bean.getAttributes().put("a", "aaa");
		bean.getAttributes().put("b", "bbbb");
		bean.getAttributes().put("c", "cccc");
		for(int i=0;i<100;i++) {
			bean.getAttributes().put(i+"", i+"");
		}
		long startTime = System.currentTimeMillis();
		for(int i=0;i<10000;i++) {
			daoByKey.getDocById("123456789", TestMongoDbBean.class);
		}
		long endTime = System.currentTimeMillis();
		System.out.println((endTime-startTime)+"ms");
		
		
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
