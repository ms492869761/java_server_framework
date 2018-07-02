package com.game.core.db.mongospring;

import java.util.List;

public class MongoConfig {
	
	private List<BindConfig> cluster;
	
	private int connPreHost =32;
	
	private String replica="";
	
	private String dbName;
	
	private String username;
	
	private String password;
	
	private int socketTimeout= 5000;
	
	private String authDBName="admin";
	
	private int threadsAllowedToBlockForConnectionMultiplier = 16;

	public List<BindConfig> getCluster() {
		return cluster;
	}

	public void setCluster(List<BindConfig> cluster) {
		this.cluster = cluster;
	}

	public int getConnPreHost() {
		return connPreHost;
	}

	public void setConnPreHost(int connPreHost) {
		this.connPreHost = connPreHost;
	}

	public String getReplica() {
		return replica;
	}

	public void setReplica(String replica) {
		this.replica = replica;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public String getAuthDBName() {
		return authDBName;
	}

	public void setAuthDBName(String authDBName) {
		this.authDBName = authDBName;
	}

	public int getThreadsAllowedToBlockForConnectionMultiplier() {
		return threadsAllowedToBlockForConnectionMultiplier;
	}

	public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
		this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
	}

	@Override
	public String toString() {
		return "MongoConfig [cluster=" + cluster + ", connPreHost=" + connPreHost + ", replica=" + replica + ", dbName="
				+ dbName + ", username=" + username + ", password=" + password + ", socketTimeout=" + socketTimeout
				+ ", authDBName=" + authDBName + ", threadsAllowedToBlockForConnectionMultiplier="
				+ threadsAllowedToBlockForConnectionMultiplier + "]";
	}
	
	
	
	
}
