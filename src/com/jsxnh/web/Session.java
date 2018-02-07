package com.jsxnh.web;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Session {

    private String id;
    public Map<String,Object> attributes = new HashMap<>();
    public static Map<String,Session> SessionMap = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public void addAttribute(String s,Object o){
        attributes.put(s,o);
    }

    public void removeAttribute(String key){
        attributes.remove(key);
    }

    public Map getAttributes(){
        return attributes;
    }

    public boolean iscontainAttribute(String key){
        if(attributes.containsKey(key))
            return true;
        return false;
    }

    public Object getAttribute(String key){
        if(iscontainAttribute(key))
            return attributes.get(key);
        return null;
    }


    public static synchronized String generatorId(){
        return UUID.randomUUID().toString();
    }

    public static void addSession(String id,Session session){
        SessionMap.put(id,session);
    }

    public static Session getSession(String id){
        return SessionMap.get(id);
    }
}
