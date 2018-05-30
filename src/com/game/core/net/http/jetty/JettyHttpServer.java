package com.game.core.net.http.jetty;

import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Server;

/**
 * Created by icyleaf on 18/5/17.
 */
public class JettyHttpServer {

    private int port = 8080;
    private String host = "0.0.0.0";

    private Server server;

    protected JettyHandlerManager handlerManager;

    public JettyHttpServer(int port) {
        this.port = port;
        handlerManager = new JettyHandlerManager();
    }

    public JettyHttpServer(String host, int port) {
        this.host = host;
        this.port = port;
        handlerManager = new JettyHandlerManager();
    }

    /**
     * 获取{@link JettyHandlerManager}，管理请求的映射处理关系
     *
     * @return
     */
    public JettyHandlerManager getHandlerManager() {
        return handlerManager;
    }

    public void start() throws Exception {
        InetSocketAddress addr = new InetSocketAddress(host, port);
        server = new Server(addr);
        server.setHandler(handlerManager);
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public static void main(String[] args) throws Exception {
        JettyHttpServer server = new JettyHttpServer("127.0.0.1", 9999);
        server.getHandlerManager()
                .addHandlersByPackage(JettyHttpServer.class.getPackage().getName());
        server.start();
    }
}
