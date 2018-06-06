package com.game.core.common.email;

public class MailServiceConfig {
	
	private String mailServerUrl;
	
	private String mailServerPort;
	
	private String mailAccount;
	
	private String password;
	
	private String sender;
	
	private boolean proxySet;
	
	private String proxyType;
	
	private String proxyHost;
	
	private String proxyPort;

	public String getMailServerUrl() {
		return mailServerUrl;
	}

	public void setMailServerUrl(String mailServerUrl) {
		this.mailServerUrl = mailServerUrl;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public String getMailAccount() {
		return mailAccount;
	}

	public void setMailAccount(String mailAccount) {
		this.mailAccount = mailAccount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public boolean isProxySet() {
		return proxySet;
	}

	public void setProxySet(boolean proxySet) {
		this.proxySet = proxySet;
	}

	public String getProxyType() {
		return proxyType;
	}

	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}
	
}
