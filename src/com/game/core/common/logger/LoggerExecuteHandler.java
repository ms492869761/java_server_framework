package com.game.core.common.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerExecuteHandler {
	
	private static LoggerExecuteHandler instance=new LoggerExecuteHandler();
	
	private LoggerExecuteHandler() {
		
	}
	
	public static LoggerExecuteHandler getInstance() {
		return instance;
	}
	
	private Logger system=LogManager.getLogger("system");
	
	private Logger debug=LogManager.getLogger("debug");
	
	private Logger error=LogManager.getLogger("error");
	
	public void dealInfoLogger(String message) {
		system.info(message);
	}
	
	public void dealInfoLogger(String message,Object...strings ) {
		system.info(message,strings);
	}
	
	public void dealWarnLogger(String message) {
		system.warn(message);
	}
	
	public void dealWarnLogger(String message,Object...objects) {
		system.warn(message, objects);
	}
	
	public void dealDebugLogger(String message) {
		debug.debug(message);
	}
	
	public void dealDebugLogger(String message,Object...objects) {
		debug.debug(message,objects);
	}
	
	public void dealExceptionLogger(String message,Throwable throwable) {
		error.error(message,throwable);
	}
	
	public static void main(String[] args) {
		LoggerExecuteHandler.getInstance().dealInfoLogger("asdfasdfasdf");
	}
	
}
