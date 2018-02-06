package com.jsxnh.web;

import java.util.HashMap;
import java.util.Map;

public class Session {

    private int id;
    public Map<String,Object> attributes = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public void addAttribute(String s,Object o){
        attributes.put(s,o);
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

}
