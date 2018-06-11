package com.game.core.db.mongo;

import java.util.ArrayList;
import java.util.List;

public class MongoDbConfig {
	
	private String id;
	
	private List<MongoAddressBean> addressList=new ArrayList<>();
	
	private String dataBase;
	
	private String userDataBase;
	
	private String user;
	
	private String password;
	/** 主机最大连接数*/
	private int connectPreHost;
	/** socket读写*/
	private int socketTimeout;
	/** 允许阻塞等待线程的倍数*/
	private int threadsAllowedToBlockForConnectionMultiplier;
	
	private String replicaSet;
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public List<MongoAddressBean> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<MongoAddressBean> addressList) {
		this.addressList = addressList;
	}

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}
	
	public String getUserDataBase() {
		return userDataBase;
	}
	
	public void setUserDataBase(String userDataBase) {
		this.userDataBase = userDataBase;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getConnectPreHost() {
		return connectPreHost;
	}
	
	public void setConnectPreHost(int connectPreHost) {
		this.connectPreHost = connectPreHost;
	}
	
	public int getSocketTimeout() {
		return socketTimeout;
	}
	
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	
	
	public int getThreadsAllowedToBlockForConnectionMultiplier() {
		return threadsAllowedToBlockForConnectionMultiplier;
	}
	
	public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
		this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
	}
	
	public String getReplicaSet() {
		return replicaSet;
	}
	
	public void setReplicaSet(String replicaSet) {
		this.replicaSet = replicaSet;
	}
	
}
