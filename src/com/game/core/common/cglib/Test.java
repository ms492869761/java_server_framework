package com.game.core.common.cglib;

import java.util.Map;

import com.alibaba.fastjson.JSON;

public class Test extends CGLibBean {

	private String id;
	private String name;
	private String add;

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

	public String getAdd() {
		return add;
	}

	public void setAdd(String add) {
		this.add = add;
	}


	/**
	 * 千万次执行时间 11283
	 * @param args
	 */
	public static void main(String[] args) {
			String jsonString = "{\"add\":\"ccccc\",\"id\":\"aaaaa\",\"name\":\"bbbbbb\"}";
			System.out.println(jsonString);
			Test createCGLibBean = CGLibBeanManager.createCGLibBean(Test.class, jsonString);
			Map<String, Object> diffData = createCGLibBean.getDiffData();
			System.out.println(diffData);
			System.out.println(JSON.toJSONString(createCGLibBean));
			createCGLibBean.setName("wzy");
			Map<String, Object> diffData2 = createCGLibBean.getDiffData();
			System.out.println(JSON.toJSONString(diffData2));
			System.out.println(JSON.toJSONString(createCGLibBean));
	}

}
