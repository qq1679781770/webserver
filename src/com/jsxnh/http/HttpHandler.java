package com.jsxnh.http;

import com.jsxnh.annotation.*;
import com.jsxnh.http.abs.Context;
import com.jsxnh.http.handler.InterErrorHandler;
import com.jsxnh.http.handler.NotFoundHandler;
import com.jsxnh.http.handler.StaticHandler;
import com.jsxnh.util.LoggerUtil;
import com.jsxnh.web.Controller;
import com.jsxnh.web.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpHandler {


    public static Logger logger = LoggerUtil.getLogger(HttpHandler.class);

    public static void init(Context context){
        //((HttpRequest)context.getRequest()).init();
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
            if(c == StaticHandler.class){
                m.invoke(c.newInstance(),request,response);
            }else {
                if(c.isAnnotationPresent(Interceptor.class)){
                    try {
                        Map intermap = context.getServerConfig().getInterceptorMap();
                        Class interClass = (Class) intermap.get(((Interceptor)c.getAnnotation(Interceptor.class)).value());
                        Method method = interClass.getDeclaredMethod("preHandler",HttpRequest.class,HttpResponse.class);
                        boolean value = (boolean) method.invoke(((Class)context.getServerConfig().getInterceptorMap().get((String)((Interceptor)c.getAnnotation(Interceptor.class)).value())).newInstance(),request,response);
                        if(!value)
                            return;
                    } catch (NoSuchMethodException e) {
                        logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
                    }
                }
                RequestMapping requestMapping = (RequestMapping)m.getAnnotation(RequestMapping.class);
                if(requestMapping.method()!=request.getMethod()){
                    NotFoundHandler.sendResponse(context);
                    return;
                }
                if(requestMapping.produce()!=null||!requestMapping.produce().equals("")){
                    String[] produces = requestMapping.produce().split(";");
                    response.setContent_type(produces[0]);
                    if(produces.length>1){
                        response.setCharset(produces[1].substring(produces[1].indexOf("=")+1));
                    }
                }

                Parameter[] parameters = m.getParameters();
                Object[] objects = new Object[parameters.length];
                for(int i=0;i<parameters.length;i++){
                    Parameter p = parameters[i];
                    if(p.getAnnotation(RequestBody.class)!=null){
                        objects[i] = new String(request.getRequestbody());
                    }else if(p.getAnnotation(Requestparam.class)!=null){
                        String value = ((Requestparam)p.getAnnotation(Requestparam.class)).value();
                        objects[i] = request.getHeaderParams().get(value);
                    }else {
                        Class aClass = p.getType();
                        if(aClass==HttpRequest.class){
                            objects[i] = request;
                        }else if(aClass==HttpResponse.class){
                            objects[i] = response;
                        }
                    }
                }
                Class returntype = m.getReturnType();
                try {
                    if (returntype == void.class) {
                        m.invoke(c.newInstance(), objects);
                    } else if (returntype == String.class) {
                        if (m.isAnnotationPresent(ResponseBody.class)) {
                            response.sendResponseBody((String) m.invoke(c.newInstance(), objects));
                        } else {
                            response.sendResponseView((String) m.invoke(c.newInstance(), objects));
                        }
                    } else if (returntype == ModelAndView.class) {
                        response.sendResponse((ModelAndView) m.invoke(c.newInstance(), objects));
                    }
                }catch (Exception e){
                    logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
                    InterErrorHandler.sendResponse(context);
                }
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
