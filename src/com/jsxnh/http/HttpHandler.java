package com.jsxnh.http;

import com.jsxnh.http.abs.Context;
import com.jsxnh.web.Controller;

import java.util.Map;

public class HttpHandler {

    public static void init(Context context){

        String url = context.getRequest().getUri();
        Map handlerMap = context.getServerConfig().getRouter().getRouterMap();

        if(!handlerMap.containsKey(url)){

        }

    }

}
