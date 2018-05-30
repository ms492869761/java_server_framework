package com.game.core.net.http.jetty;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.core.net.http.HttpHandlerManager;
import com.game.core.net.http.HttpMethodConfig;
import com.game.core.net.http.HttpRequestMethod;

/**
 * Created by icyleaf on 19/5/17.
 */
public class JettyHandlerManager extends HandlerCollection {

    private static Logger logger = LoggerFactory.getLogger(JettyHandlerManager.class);

    private HttpHandlerManager handlers = new HttpHandlerManager();

    private ServletContextHandler servletContextHandler = new ServletContextHandler();

    /**
     * 初始化handler的映射关系
     *
     * @param packageName handler所在的包名
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void addHandlersByPackage(String packageName) {
        handlers.addHandlersByPackage(packageName);
    }

    protected void doStart() throws Exception {
        addHandler(servletContextHandler);
        super.doStart();
    }

    public void addServlet(String path, Class<? extends Servlet> servletClass) {
        servletContextHandler.addServlet(servletClass, path);
    }

    /**
     * 处理jetty的回调请求
     *
     * @param target
     * @param baseRequest
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void handle(String target, Request baseRequest, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        if (handlers.getHandlers().containsKey(target)) {
            String resStr = "404 not found.";
            HttpRequestMethod method = HttpRequestMethod.valueOf(request.getMethod());
            HttpMethodConfig httpMethodConfig = handlers.getHandlers().get(target);
            response.setStatus(HttpServletResponse.SC_OK);
            if (httpMethodConfig.getHttpMethod().equals(method)
                    || httpMethodConfig.getHttpMethod().equals(HttpRequestMethod.UNLIMITED)) {
                try {
                    resStr = httpMethodConfig.invoke(request);
                } catch (Exception e) {
                    logger.error("handle exception:", e);
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(resStr);
            response.flushBuffer();
        } else {
            super.handle(target, baseRequest, request, response);
        }
    }

}
