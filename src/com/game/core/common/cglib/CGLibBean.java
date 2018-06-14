package com.game.core.common.cglib;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;


public class CGLibBean {
	private Map<String, Object> diffDataMap=new HashMap<>();
	
	public void addDiffData(String key,Object value) {
		diffDataMap.put(key, value);
	}
	@JSONField(serialize=false)
	public Map<String, Object> getDiffData() {
		return diffDataMap;
	}
	@JSONField(serialize=false)
	public void clearDiffData() {
		diffDataMap.clear();
	}
	
}
