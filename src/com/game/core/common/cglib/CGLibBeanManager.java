package com.game.core.common.cglib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.game.core.common.cglib.annotation.CGDiffMethodName;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


/**
 * 动态代理类管理器
 * @author wangzhiyuan
 *
 */
public class CGLibBeanManager {
	
	/**
	 * 使用json字符串反序列化出对象
	 * @param cls
	 * @param json
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	public static <T extends CGLibBean> T createCGLibBean(Class<T> cls,String json) {
		Enhancer enhancer=new Enhancer();
		enhancer.setSuperclass(cls);
		enhancer.setCallback(new MethodInterceptor() {
			
			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				String methodName = method.getName();
				if(methodName.startsWith("set")) {
					if(args.length!=1) {
						return proxy.invokeSuper(obj, args);
					}
					if(method.getParameterCount()!=1) {
						return proxy.invokeSuper(obj, args);
					}
					String attributeName;
					CGDiffMethodName cgDiffMethodName = method.getAnnotation(CGDiffMethodName.class);
					if(cgDiffMethodName!=null) {
						attributeName=cgDiffMethodName.Name();
					} else {
						String substring = methodName.substring(3);
						StringBuilder sb=new StringBuilder(substring);
						if(Character.isUpperCase(sb.charAt(0))) {
							sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
						}
						String fieldName = sb.toString();
						attributeName=fieldName;
					}
					if(attributeName==null) {
						return proxy.invokeSuper(obj, args);
					}
					T t=(T)obj;
					t.addDiffData(attributeName, args[0]);
				}
				return proxy.invokeSuper(obj, args);
			}
		});
		T javaObject = JSON.toJavaObject(JSON.parseObject(json),cls);
		T create = (T)enhancer.create();
		BeanUtils.copyProperties(javaObject, create, "diffDataMap");
		create.clearDiffData();
		return create;
	}
	
	public static void main(String[] args) {
		String name="getUserLastLoginTime";
		String substring = name.substring(3);
		System.out.println(substring);
		StringBuilder sb=new StringBuilder(substring);
		if(Character.isUpperCase(sb.charAt(0))) {
			sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		}
		String fieldName = sb.toString();
		System.out.println(fieldName);
	}
	
}
