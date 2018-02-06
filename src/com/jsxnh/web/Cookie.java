package com.jsxnh.web;

import java.util.HashMap;
import java.util.Map;

public class Cookie {
    String cookies;

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Map<String,String> attributes = new HashMap<>();

    public Cookie(String s){
        this.cookies = s;
        init();
    }

    public void init(){
        String[] s = cookies.split(";");
        for(String ss:s){
            attributes.put(ss.substring(0,ss.indexOf("=")),ss.substring(ss.indexOf("=")+1));
        }
    }

}
