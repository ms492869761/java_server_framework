package com.game.core.net.http;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by icyleaf on 19/5/17.
 */
public class HttpMethodConfig {
    private Method method;
    private IHttpRequestHandler handler;
    private HttpRequestMethod httpMethod;

    public HttpMethodConfig(IHttpRequestHandler handler, Method method,
            HttpRequestMethod httpMethod) {
        this.handler = handler;
        this.method = method;
        this.httpMethod = httpMethod;
    }

    public String invoke(HttpServletRequest request)
            throws InvocationTargetException, IllegalAccessException {
        return (String) method.invoke(handler, request);
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public IHttpRequestHandler getHandler() {
        return handler;
    }

    public void setHandler(IHttpRequestHandler handler) {
        this.handler = handler;
    }

    public HttpRequestMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpRequestMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

}
