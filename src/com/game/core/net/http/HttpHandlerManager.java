package com.game.core.net.http;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.core.net.http.annotation.HttpLogicHandler;
import com.game.core.net.http.annotation.HttpLogicMethod;

/**
 * HttpHandlerManager
 *
 * @author zhenkun.wei
 */
public class HttpHandlerManager {

    private static Logger logger = LoggerFactory.getLogger(HttpHandlerManager.class);

    private Map<String, HttpMethodConfig> handlers = new HashMap<>();

    /**
     * 鍒濆鍖杊andler鐨勬槧灏勫叧绯�
     *
     * @param packageName handler鎵�鍦ㄧ殑鍖呭悕
     */
    @SuppressWarnings("unchecked")
    public void addHandlersByPackage(String packageName) {
        try {
            Reflections reflections = new Reflections(packageName);
            Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(HttpLogicHandler.class);

            for (Class<?> clz : classSet) {
                HttpLogicHandler anno = clz.getAnnotation(HttpLogicHandler.class);
                String basePath = anno.basePath();
                IHttpRequestHandler handler = (IHttpRequestHandler) clz.newInstance();

                Set<Method> methods = ReflectionUtils.getAllMethods(clz,
                        ReflectionUtils.withAnnotation(HttpLogicMethod.class));
                for (Method method : methods) {
                    HttpLogicMethod methodAnnotation = method.getAnnotation(HttpLogicMethod.class);
                    // check annotation
                    String realPath = new StringBuilder().append(basePath)
                            .append(methodAnnotation.path()).toString();
                    HttpMethodConfig httpMethodConfig = new HttpMethodConfig(handler, method,
                            methodAnnotation.httpMethod());
                    addHandlerMethod(realPath, httpMethodConfig);
                }
            }
        } catch (Exception e) {
            logger.error("addHandlersByPackage errro:", e);
            e.printStackTrace();
        }
    }

    public void addHandlerMethod(String path, HttpMethodConfig httpMethodConfig) {
        if (!checkPathMap(path)) {
            return;
        }
        handlers.put(path, httpMethodConfig);
    }

    protected boolean checkPathMap(String path) {
        if (handlers.containsKey(path)) {
            logger.error("HttpHandler has contains:{}", path);
            return false;
        }
        return true;
    }

    public Map<String, HttpMethodConfig> getHandlers() {
        return handlers;
    }

}
