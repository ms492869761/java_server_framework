package com.game.core.net.http.jetty;

import javax.servlet.http.HttpServletRequest;

import com.game.core.net.http.HttpRequestMethod;
import com.game.core.net.http.IHttpRequestHandler;
import com.game.core.net.http.annotation.HttpLogicHandler;
import com.game.core.net.http.annotation.HttpLogicMethod;

/**
 * Created by icyleaf on 19/5/17.
 */
@HttpLogicHandler(basePath = "/account")
public class DemoHandler implements IHttpRequestHandler {

    @HttpLogicMethod(httpMethod = HttpRequestMethod.GET, path = "/login", desc = "登录")
    public String login(HttpServletRequest request) {
        return "Hello! You have logined!";
    }

    @HttpLogicMethod(httpMethod = HttpRequestMethod.UNLIMITED, path = "/logout", desc = "登出")
    public String logout(HttpServletRequest request) {
        return "Goodbye! You have logouted!";
    }
}
