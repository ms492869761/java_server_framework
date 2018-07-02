package com.game.core.common.utils;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 网络工具
 * @author wangzhiyuan
 *
 */
public class NetUtils {
	
	/**
	 * 查看服务器端口是否允许使用
	 * @param port
	 * @return
	 */
	public static boolean checkPortEnable(int port) {
		try {
			Socket socket=new Socket();
			socket.setSoLinger(false, 0);
			socket.bind(new InetSocketAddress("0.0.0.0", port));
			socket.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	
}
