package com.jsxnh.http.api;

import com.jsxnh.config.ServerConfig;
import com.jsxnh.http.HttpMethod;
import com.jsxnh.http.abs.Context;
import com.jsxnh.web.Cookie;
import com.jsxnh.web.Session;

import java.util.Map;

public interface Request {

    public Object getAttribute(String key);
    public boolean isContain(String attribute);
    public Context getContext();
    public String getUrl();
    public String getUri();
    public Map<String,Object> getAttributes();
    public Session getSession();
    public Cookie getCookie();
    public Boolean isCookie();
    public ServerConfig getServerConfig();
    public HttpMethod getMethod();
    public void setContext(Context context);

}
