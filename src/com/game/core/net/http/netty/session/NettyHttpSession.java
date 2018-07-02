package com.game.core.net.http.netty.session;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;

import com.game.core.common.utils.UUIDService;

/**
 * netty session 会话
 * @author wangzhiyuan
 *
 */
@SuppressWarnings("deprecation")
public class NettyHttpSession implements HttpSession{

	public static String SESSION_COOKIE_NAME="JSESSIONID";
	/** session保存的数据信息*/
	private Map<String, Object> attributes=new LinkedHashMap<>();
	/** 是否已关闭*/
	private boolean invalid=false;
	/** 会话创建时间*/
	private long creationTime=System.currentTimeMillis();
	/** 最后通信时间*/
	private long lastAccessedTime=System.currentTimeMillis();
	/** */
	private ServletContext servletContext;
	/** 会话ID*/
	private String id;
	
	private int maxInactiveInterval;
	
	private boolean isNew=true;
	
	
	
	public NettyHttpSession() {
		this(null);
	}
	
	public NettyHttpSession(ServletContext servletContext) {
		this(servletContext, null);
	}
	
	public NettyHttpSession(ServletContext servletContext,String id) {
		this.servletContext=servletContext;
		this.id = (id!=null?id:UUIDService.getInstance().getId(1)+"");
	}
	
	@Override
	public Object getAttribute(String arg0) {
		return attributes.get(arg0);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return Collections.enumeration(new LinkedHashSet<String>(this.attributes.keySet()));
	}

	@Override
	public long getCreationTime() {
		return this.creationTime;
	}

	@Override
	public String getId() {
		return this.id;
	}
	
	public String changeSessionId() {
		this.id= UUIDService.getInstance().getId(1)+"";
		return this.id;
	}
	
	@Override
	public long getLastAccessedTime() {
		return this.lastAccessedTime;
	}

	@Override
	public int getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	@Override
	public ServletContext getServletContext() {
		return this.servletContext;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		throw new UnsupportedOperationException("getSessionContext");
	}

	@Override
	public Object getValue(String arg0) {
		return getAttribute(arg0);
	}

	@Override
	public String[] getValueNames() {
		return this.attributes.keySet().toArray(new String[this.attributes.size()]);
	}

	@Override
	public void invalidate() {
		this.invalid=true;
		clearAttributes();
	}

	@Override
	public boolean isNew() {
		return this.isNew;
	}

	@Override
	public void putValue(String arg0, Object arg1) {
		setAttribute(arg0, arg1);
		
	}

	@Override
	public void removeAttribute(String arg0) {
		Object value = this.attributes.remove(arg0);
		if(value instanceof HttpSessionBindingListener) {
			((HttpSessionBindingListener) value).valueUnbound(new HttpSessionBindingEvent(this, arg0, value));
		}
	}

	@Override
	public void removeValue(String arg0) {
		removeAttribute(arg0);
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		if(arg1!=null) {
			this.attributes.put(arg0, arg1);
			if(arg1 instanceof HttpSessionBindingListener) {
				((HttpSessionBindingListener) arg1).valueBound(new HttpSessionBindingEvent(this, arg0, arg1));
			}
		} else {
			this.attributes.remove(arg0);
		}
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		this.maxInactiveInterval=arg0;
		
	}
	
	public void clearAttributes() {
		Set<Entry<String,Object>> entrySet = this.attributes.entrySet();
		Iterator<Entry<String, Object>> iterator = entrySet.iterator();
		while(iterator.hasNext()) {
			Entry<String, Object> next = iterator.next();
			String key = next.getKey();
			Object value = next.getValue();
			iterator.remove();
			if(value instanceof HttpSessionBindingListener) {
				((HttpSessionBindingListener)value).valueUnbound(new HttpSessionBindingEvent(this, key, value));
			}
		}
	}
	
	public void assertIsValid() {
		if(invalid) {
			throw new IllegalStateException("The session has already been invalidated");
		}
	}
	
	public void access() {
		this.lastAccessedTime=System.currentTimeMillis();
		this.isNew=false;
	}
	
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
}
