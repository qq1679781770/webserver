package com.jsxnh.http;

import com.jsxnh.http.abs.Context;
import com.jsxnh.http.handler.NotFoundHandler;
import com.jsxnh.util.LoggerUtil;
import com.jsxnh.web.Controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpHandler {


    public static Logger logger = LoggerUtil.getLogger(HttpHandler.class);

    public static void init(Context context){
        ((HttpRequest)context.getRequest()).init();
        String url = context.getRequest().getUri();
        Map handlerMap = context.getServerConfig().getRouter().getRouterMap();
        if(!handlerMap.containsKey(url)){
            NotFoundHandler.sendResponse(context);
        }else{

            parseController((Controller) handlerMap.get(url),context);

        }

    }


    public static void parseController(Controller controller,Context context){
        HttpRequest request = (HttpRequest) context.getRequest();
        HttpResponse response = (HttpResponse) context.getResponse();
        Method m = controller.getMethod();
        try {
            Class c = Class.forName(controller.getClassname());
            Class returetype = m.getReturnType();
            if(returetype==void.class){
                m.invoke(c.newInstance(),request,response);
            }


        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        } catch (IllegalAccessException e) {
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        } catch (InstantiationException e) {
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        } catch (InvocationTargetException e) {
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        }


    }


}
