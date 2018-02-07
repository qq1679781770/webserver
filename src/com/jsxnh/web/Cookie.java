package com.jsxnh.web;

import java.util.HashMap;
import java.util.Map;

public class Cookie {
    String cookies;
    public static final String SESSION = "SessionID";

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Map<String,String> attributes = new HashMap<>();

    public Cookie(String s){
        this.cookies = s;
        init();
    }

    public Cookie(){

    }

    public void init(){
        String[] s = cookies.split(";");
        for(String ss:s){
            attributes.put(ss.substring(0,ss.indexOf("=")),ss.substring(ss.indexOf("=")+1));
        }
    }

    public String getAttriute(String key){
        if(isContain(key)){
            return attributes.get(key);
        }
        return null;
    }

    public boolean isContain(String key){
        if(attributes.containsKey(key))
            return true;
        return false;
    }

    public String getCookieStr(){
        StringBuilder stringBuilder = new StringBuilder();
        for(String key:attributes.keySet()){
            stringBuilder.append(key+"="+attributes.get(key)+";");
        }
        String s = stringBuilder.toString();
        return s.substring(0,s.length()-1);
    }

    public void addAttribue(String key,String value){
        attributes.put(key,value);
    }
}
