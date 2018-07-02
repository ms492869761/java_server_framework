package com.game.core.net.tcp.netty;

import java.io.IOException;

import com.game.core.common.logger.LoggerExecuteHandler;
import com.game.core.common.utils.NetUtils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyTcpService{
	
	private Channel bossChannel;
	/** 处理客户端的连接请求*/
	private NioEventLoopGroup bossGroup;
	/** 处理客户端的IO请求*/
	private NioEventLoopGroup workerGroup;
	
	private ChannelHandler channelHandler;
	
	@SuppressWarnings("deprecation")
	public boolean start(int port,ChannelHandler channelHandler) throws IOException {
		if(!NetUtils.checkPortEnable(port)) {
			throw new IOException("the port : "+port+"  has bean enabled");
		}
		try {
			this.channelHandler=channelHandler;
			// 连接监听线程启用一个
			bossGroup=new NioEventLoopGroup(1);
			// 处理IO线程为处理器线程个数的两倍
			workerGroup=new NioEventLoopGroup(Runtime.getRuntime().availableProcessors()*2);
			
			ServerBootstrap bootstrap=new ServerBootstrap().group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(this.channelHandler)
					.childOption(ChannelOption.SO_KEEPALIVE, true)// 系统是否向客户端发送确认状态心跳包
					.childOption(ChannelOption.TCP_NODELAY, true)// 不安比Nagle算法
					.childOption(ChannelOption.SO_LINGER, 0)// 连接关闭时，尝试吧未发送完成的数据继续发送的时间
					.childOption(ChannelOption.SO_SNDBUF, 4096)// 系统socket发送的buffer大小
					.childOption(ChannelOption.SO_RCVBUF, 2048)// 接受的buff大小
					.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
					.childOption(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
					.option(ChannelOption.SO_REUSEADDR, true)// 是否允许端口重用
					.option(ChannelOption.SO_BACKLOG, 10000);// 最大等待连接的连接数量
			workerGroup.setIoRatio(100);
			bossChannel=bootstrap.bind(port).sync().channel();
			LoggerExecuteHandler.getInstance().dealInfoLogger("NettyTcp监听服务启动成功，端口:{}",port);
			return true;
		} catch (Exception e) {
			LoggerExecuteHandler.getInstance().dealExceptionLogger("开启端口:"+port+" 失败，退出程序", e);
			return false;
		}
	}
	
	
	public boolean stop() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		bossChannel.close().awaitUninterruptibly();
		return true;
	}
	
}
