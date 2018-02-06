package com.jsxnh.config;

import com.jsxnh.exception.NotFoundInteceptor;
import com.jsxnh.exception.SameRouterException;
import com.jsxnh.exception.ServerConfigNotFoundException;
import com.jsxnh.util.LoggerUtil;
import com.jsxnh.util.XMLUtil;
import com.jsxnh.web.Router;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConfig {

    private String webxmlfilename;
    private File webxmlfile;
    private Integer port;
    private String charset = "utf-8";
    public static String logPath = null;
    public static Logger logger = LoggerUtil.getLogger(ServerConfig.class);
    public Map<String,Class> InterceptorMap = new HashMap<>();
    public  Router getRouter() {
        return router;
    }

    public static Router router;

    public ServerConfig(){
        router = new Router();
    }

    public ServerConfig(String webxmlfilename){
        this.webxmlfilename = webxmlfilename;
        router = new Router();
        try {
            XMLUtil.parse(this);
        } catch (ServerConfigNotFoundException e) {
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        }
    }

    public void addRouter(String classname){
        try {
            router.addRouter(Class.forName(classname));
        } catch (SameRouterException e) {
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        }
    }

    public void addInterceptor(String name,String classname){
        try{
            Class c = Class.forName(classname);
            InterceptorMap.put(name,c);
        }catch (Exception e){
            try {
                throw new NotFoundInteceptor(classname);
            }catch (NotFoundInteceptor notFoundInteceptor){
                logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(notFoundInteceptor));
            }
        }
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getPort() {
        if(port==null||port==0)
            return getDefaultPort();
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }



    public Integer getDefaultPort(){
        return 8001;
    }

    public String getWebxmlfilename() {
        return webxmlfilename;
    }

    public void setWebxmlfilename(String webxmlfilename) {
        this.webxmlfilename = webxmlfilename;
    }

    public File getWebxmlfile() {
        return webxmlfile;
    }

    public void setWebxmlfile(File webxmlfile) {
        this.webxmlfile = webxmlfile;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "webxmlfilename='" + webxmlfilename + '\'' +
                ", port=" + port +
                ", charset='" + charset + '\'' +
                '}';
    }

    public Map<String, Class> getInterceptorMap() {
        return InterceptorMap;
    }


}
