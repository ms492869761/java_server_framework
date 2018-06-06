package com.game.core.common.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SimpleAuthenticator extends Authenticator {
	
	private String userName;
	
	private String password;
	
	public SimpleAuthenticator(String userName,String password) {
		this.userName=userName;
		this.password=password;
	}
	
	
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
	
	
}
