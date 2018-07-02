package com.game.core.db.mongospring;

/**
 * 地址配置
 * @author wangzhiyuan
 *
 */
public class BindConfig {
	/** host*/
	private String host;
	/** 端口*/
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
