package com.jsxnh.web;

import com.jsxnh.annotation.RequestMapping;
import com.jsxnh.exception.SameRouterException;
import com.jsxnh.http.HttpRequest;
import com.jsxnh.http.HttpResponse;
import com.jsxnh.http.handler.StaticHandler;
import com.jsxnh.util.LoggerUtil;
import com.jsxnh.util.PathUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Router {



    private HashMap<String,Controller> routerMap = new HashMap<>();

    public HashMap<String, Controller> getRouterMap() {
        return routerMap;
    }
    private static final Logger LOGGER = LoggerUtil.getLogger(Router.class);

    public void addRouter(Class classz) throws SameRouterException {
        Method[] methods = classz.getDeclaredMethods();
        for(Method m:methods){
            if(m.isAnnotationPresent(RequestMapping.class)){
                RequestMapping requestMapping = (RequestMapping)m.getAnnotation(RequestMapping.class);
                String url = requestMapping.value();
                if(routerMap.containsKey(url)){
                    throw new SameRouterException(url);
                }else{
                    routerMap.put(url,new Controller(classz.getName(),m));
                    LOGGER.info("add handler url:"+url+" class:"+classz.getName());

                }

            }
        }
    }

    public void addRouter(String staticfile){
        File dir = new File(staticfile);
        if(dir.isDirectory()){
            if(dir.exists()){
                LinkedList<File> dirlist = new LinkedList<>();
                LinkedList<File> filelist = new LinkedList<>();
                File[] files = dir.listFiles();
                for(File f:files){
                    if(f.isDirectory()){
                        dirlist.add(f);
                    }else {
                        filelist.add(f);
                    }
                }
                File temp_file;
                while (!dirlist.isEmpty()){
                    temp_file = dirlist.removeFirst();
                    files = temp_file.listFiles();
                    for(File f:files){
                        if(f.isDirectory()){
                            dirlist.add(f);
                        }else {
                            filelist.add(f);
                        }
                    }
                }
                for(File f:filelist){
                    String url = PathUtil.getTempPath(f.getAbsolutePath());
                    if(routerMap.containsKey(url)){
                        continue;
                    }
                    try {
                        Method m = StaticHandler.class.getDeclaredMethod("dealStatic", HttpRequest.class, HttpResponse.class);
                        routerMap.put(url,new Controller("com.jsxnh.http.handler.StaticHandler",m));
                    } catch (NoSuchMethodException e) {
                        LOGGER.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
                    }
                }
                LOGGER.info("add staticpath:"+dir.getName());
            }
        }else{
            if(dir.exists()){
                String url = PathUtil.getTempPath(dir.getAbsolutePath());
                if(routerMap.containsKey(url)){

                }else{
                    try {
                        Method m = StaticHandler.class.getDeclaredMethod("dealStatic", HttpRequest.class, HttpResponse.class);
                        routerMap.put(url,new Controller("com.jsxnh.http.handler.StaticHandler",m));
                    } catch (NoSuchMethodException e) {
                        LOGGER.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
                    }
                }
            }
        }
    }

}
