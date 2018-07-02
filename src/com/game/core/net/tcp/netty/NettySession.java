package com.game.core.net.tcp.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.AttributeKey;

/**
 * 会话
 * @author wangzhiyuan
 *
 */
public class NettySession {
	
	protected ChannelHandlerContext ctx;
	/** 会话键值*/
	public static AttributeKey<NettySession> KEY_SESSION= AttributeKey.valueOf("session.key");
	
	public NettySession(ChannelHandlerContext ctx) {
		this.ctx=ctx;
		this.ctx.channel().attr(KEY_SESSION).set(this);
	}
	
	public void writeSocket(byte[] msg) {
		ctx.writeAndFlush(msg);
	}
	
	public void writeWebSocket(byte[] msg) {
		ByteBuf copiedBuffer = Unpooled.copiedBuffer(msg);
		BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(copiedBuffer);
		ctx.writeAndFlush(binaryWebSocketFrame);
	}
	
	public static NettySession getSession(ChannelHandlerContext ctx) {
		NettySession nettySession = ctx.channel().attr(KEY_SESSION).get();
		return nettySession;
	}
	
	public String getId() {
		return ctx.channel().id().asLongText();
	}
	
	public void close() {
		this.ctx.close();
	}
	
	
	
}
