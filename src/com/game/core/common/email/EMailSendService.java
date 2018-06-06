package com.game.core.common.email;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.game.core.common.logger.LoggerExecuteHandler;

/**
 * 发送EMAIL
 * @author wangzhiyuan
 *
 */
public class EMailSendService {
	
	private static EMailSendService instance=new EMailSendService();
	
	private EMailSendService() {
		
		MailServiceConfig config=new MailServiceConfig();
		config.setMailAccount("wangzhiyuan@playcrab.com");
		config.setPassword("wang^@^$88");
		config.setMailServerUrl("smtp.partner.outlook.cn");
		config.setMailServerPort("587");
		config.setProxySet(false);
		config.setSender("wangzhiyuan@playcrab.com");
		
		try {
			addMailServiceTemp("default", config);
		} catch (MessagingException e) {
			LoggerExecuteHandler.getInstance().dealExceptionLogger("init mail service exception", e);
		}
	}
	
	public static EMailSendService getInstance() {
		return instance;
	}
	
	private Map<String, MailServiceConfig> mailServiceConfigMap=new HashMap<>();
	
	private Map<String, Session> mailServiceSessionMap=new HashMap<>();
	
	private Map<String, Transport> mailServiceTransportMap=new HashMap<>();
	
	private Set<String> adminAddress=new HashSet<>();
	
	public void addMailServiceTemp(String method,MailServiceConfig config) throws MessagingException {
		mailServiceConfigMap.put(method, config);
		Properties properties = getMailServiceProp(method);
		SimpleAuthenticator simpleAuthenticator = new SimpleAuthenticator(config.getMailAccount(), config.getPassword());
		Session session = Session.getDefaultInstance(properties, simpleAuthenticator);
		session.setDebug(false);
		mailServiceSessionMap.put(method, session);
		Transport transport = session.getTransport("smtp");
		transport.connect();
		mailServiceTransportMap.put(method, transport);
	}
	
	public void addAdminAddress(String address) {
		this.adminAddress.add(address);
	}
	
	public boolean sendAdminMail(String method,String title,String content) throws AddressException, MessagingException {
		for (String address : adminAddress) {
			if(!sendMail(method, address, title, content)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean sendMail(String method,String address,String title,String content) throws AddressException, MessagingException {
		Session session = mailServiceSessionMap.get(method);
		MailServiceConfig mailServiceConfig = mailServiceConfigMap.get(method);
		Transport transport = mailServiceTransportMap.get(method);
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(mailServiceConfig.getMailAccount()));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
		MimeBodyPart mdp = new MimeBodyPart();
		mdp.setContent(content, "text/html;charset=utf-8");
		MimeMultipart mimeMultipart = new MimeMultipart();
		mimeMultipart.addBodyPart(mdp);
		message.setContent(mimeMultipart);
		message.setSubject(title);
		message.saveChanges();
		if(!transport.isConnected()) {
			transport.connect();	
		}
		LoggerExecuteHandler.getInstance().dealDebugLogger("send email to \"{}\" , title: \"{}\" , content: \"{}\"", address,title,content);
		transport.sendMessage(message, message.getAllRecipients());
        return true;
	}
	
	/**
	 * TLS方式配置
	 * @param method
	 * @return
	 */
	private Properties getMailServiceProp(String method) {
		MailServiceConfig mailServiceConfig = mailServiceConfigMap.get(method);
		Properties pro=new Properties();
		pro.setProperty("mail.smtp.auth", "true");
		pro.setProperty("mail.smtp.starttls.enable", "true");
		pro.setProperty("mail.smtp.host", mailServiceConfig.getMailServerUrl());
		pro.setProperty("mail.smtp.port", mailServiceConfig.getMailServerPort());
		return pro;
	}
	
	
	
	
	public static void main(String[] args) throws AddressException, MessagingException {
		EMailSendService.getInstance().sendMail("default", "w492869761@vip.qq.com", "test email","this a java send email");
	}
	
}
