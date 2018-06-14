package com.game.core.common.cglib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CGDiffMethodName {
	/**
	 * 对应持久化名字
	 * @return
	 */
	String Name();
}
