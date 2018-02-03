package com.jsxnh.config;

import com.jsxnh.exception.ServerConfigNotFoundException;
import com.jsxnh.util.LoggerUtil;
import com.jsxnh.util.XMLUtil;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConfig {

    private String webxmlfilename;
    private File webxmlfile;
    private Integer port;
    private String charset = "utf-8";
    public static String logPath = null;
    public static Logger logger = LoggerUtil.getLogger(ServerConfig.class);

    public ServerConfig(){

    }

    public ServerConfig(String webxmlfilename){
        this.webxmlfilename = webxmlfilename;
        try {
            XMLUtil.parse(this);
        } catch (ServerConfigNotFoundException e) {
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
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
}
