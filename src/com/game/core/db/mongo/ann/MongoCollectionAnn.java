package com.game.core.db.mongo.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface MongoCollectionAnn {
	
	/**
	 * mongo collection 
	 * @return
	 */
	String Collection();
	
}
