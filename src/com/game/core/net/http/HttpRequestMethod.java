package com.game.core.net.http;

/**
 * Created by icyleaf on 18/5/17.
 */
public enum HttpRequestMethod {
    UNLIMITED(""),
    POST("POST"),
    GET("GET"),
    PUT("PUT"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    HEAD("HEAD"),
    TRACE("TRACE");;

    private String realMethod = "";

    HttpRequestMethod(String method) {
        this.realMethod = method;
    }
}
