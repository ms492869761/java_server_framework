package com.game.core.db.mongospring;

import java.util.Set;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * mongoDB接口
 * @author wangzhiyuan
 *
 */
public class BaseRepository {
	
	private MongoTemplate mongoTemplate;
	
	public BaseRepository(MongoTemplate mongoTemplate) {
		this.mongoTemplate=mongoTemplate;
	}
	
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}
	
	public <T> void save(T doc) {
		this.mongoTemplate.save(doc);
	}
	
	public <T> void save(T doc,String collectionName) {
		this.mongoTemplate.save(doc, collectionName);
		
	}
	
	public <T> T getDocById(String id,Class<T> clz) {
		return this.mongoTemplate.findById(id, clz);
	}
	
	public <T> T getDocById(String id,Class<T> clz,String collectionName) {
		return this.mongoTemplate.findById(id, clz, collectionName);
	}
	
	public <T> T getDocById(String id,Class<T> clz,Set<String> fields) {
		Criteria cri = new Criteria("id").is(id);
		Query query = Query.query(cri);
		fields.forEach(field->{
			query.fields().include(field);
		});
		return this.mongoTemplate.find(query, clz).get(0);
	}
	
	public <T> void remove(String id,Class<T> clz) {
		Criteria criteria = new Criteria("id").is(id);
		Query query = new Query(criteria);
		this.mongoTemplate.remove(query, clz);
	}
	
	public <T> void remove(String id,String collectionName) {
		Criteria criteria = new Criteria("id").is(id);
		Query query = new Query(criteria);
		this.mongoTemplate.remove(query, collectionName);
	}
	
	public boolean exist(String id,Class<?> clz) {
		Query query=new Query();
		query.addCriteria(new Criteria("id").is(id));
		return this.mongoTemplate.exists(query, clz);
	}
	
	public long getCount(Class<?> clz) {
		Criteria cri=new Criteria();
		Query query = new Query(cri);
		return this.mongoTemplate.count(query, clz);
	}
	
	public long getCount(String collectionName) {
		Criteria cri=new Criteria();
		Query query = new Query(cri);
		return this.mongoTemplate.count(query, collectionName);
	}
	
}
