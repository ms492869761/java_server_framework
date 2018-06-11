package com.game.core.db.mongo.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * mongo持久化基本类
 * @author wangzhiyuan
 *
 */
public class BaseMongoPersistenceBean {
	@JSONField(name="_id")
	private String mongoId;
	

	public String getMongoId() {
		return mongoId;
	}
	
	public void setMongoId(String mongoId) {
		this.mongoId = mongoId;
	}
	
	
	
	
	
	
}
