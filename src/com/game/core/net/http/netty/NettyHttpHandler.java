package com.game.core.net.http.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

public class NettyHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
	
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		// TODO Auto-generated method stub
		HttpHeaders headers = msg.headers();
		
		
	}
	
	
	
	
}
