package com.game.core.db.mongo.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.game.core.common.cglib.CGLibBean;
import com.game.core.common.cglib.annotation.CGDiffMethodName;

/**
 * mongo持久化基本类
 * @author wangzhiyuan
 *
 */
public class BaseMongoPersistenceBean extends CGLibBean{
	@JSONField(name="_id")
	private String mongoId;
	

	public String getMongoId() {
		return mongoId;
	}
	
	@CGDiffMethodName(Name="_id")
	public void setMongoId(String mongoId) {
		this.mongoId = mongoId;
	}
	
	
	
	
	
	
}
