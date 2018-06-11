package com.game.core.db.mongo;

/**
 * mongoDB连接地址
 * @author wangzhiyuan
 *
 */
public class MongoAddressBean {
	
	private String host;
	
	private int port;
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
}
