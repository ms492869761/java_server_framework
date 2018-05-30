package com.game.core.net.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.game.core.net.http.HttpRequestMethod;

/**
 * 
 * @author wangzhiyuan
 *
 */
@Retention(RetentionPolicy.RUNTIME)// 
@Target({ElementType.METHOD})// 
public @interface HttpLogicMethod {
	
	HttpRequestMethod httpMethod() default HttpRequestMethod.POST;
	
	String path();
	
	String desc() default "";
	
}
