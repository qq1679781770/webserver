package com.jsxnh.http;

import com.jsxnh.config.ServerConfig;
import com.jsxnh.http.abs.Context;
import com.jsxnh.http.api.Request;
import com.jsxnh.util.LoggerUtil;
import com.jsxnh.web.Cookie;
import com.jsxnh.web.Session;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpRequest implements Request{

    private String uri;
    private String url;
    private String context_type;
    private HttpMethod method;
    private String charset;
    private String protocol;
    private Map<String,Object> attributes;
    private Map<String,String> headerParams;
    private InputStream inputStream;
    private Cookie cookie;
    private SocketChannel channel;
    private String referer;
    private Context context;
    public static Logger logger = LoggerUtil.getLogger(HttpRequest.class);

    public HttpRequest(SocketChannel sc){
        this.channel = sc;
        init();
    }


    private void init(){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            byte[] bytes = null;
            int size = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((size = channel.read(buffer)) > 0) {
                buffer.flip();
                bytes = new byte[size];
                buffer.get(bytes);
                baos.write(bytes);
                buffer.clear();
            }
            bytes = baos.toByteArray();
            if(bytes.length==0){
                return;
            }
            String requeststr = new String(bytes);
            initSchema(requeststr.split("\r\n")[0]);
            //requeststr.indexOf("\r\n\r\n");



        }catch (Exception e){
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        }

    }

    private void initSchema(String headerfirst){
        String s[] = headerfirst.split(" ");
        String method = s[0];
        if(method.equals("GET")){
            this.method = HttpMethod.GET;
        }else if(method.equals("POST")){
            this.method = HttpMethod.POST;
        }
        this.protocol = s[2];
        this.url = s[1];
        if(s[1].indexOf("?")==-1){
            this.uri = s[1];
        }else{
            this.uri = s[1].substring(0,s[1].indexOf("?"));
            String paramstr = s[1].substring(s[1].indexOf("?"));
            String[] params = paramstr.split("&");
            for(String p:params){
                headerParams.put(p.substring(0,p.indexOf("=")),p.substring(p.indexOf("=")));
                attributes.put(p.substring(0,p.indexOf("=")),p.substring(p.indexOf("=")));
            }
        }
    }


    @Override
    public Object getAttribute(String key) {
        if(isContain(key)){
            return attributes.get(key);
        }else{
            return null;
        }
    }

    @Override
    public boolean isContain(String attribute) {
        for(String key:attributes.keySet()){
            if(key.equals(attribute)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Session getSession() {
        return null;
    }

    @Override
    public Cookie getCookie() {
        return null;
    }

    @Override
    public Boolean isCookie() {
        return null;
    }

    @Override
    public ServerConfig getServerConfig() {
        return context.getServerConfig();
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
}
