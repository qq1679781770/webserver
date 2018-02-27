package com.jsxnh.web;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {



    private String viewname;
    private Map<String,Object> attributes = new HashMap<>();

    public ModelAndView(String viewname){
        this.viewname = viewname;
    }

    public void addObject(String attribute,Object o){
        attributes.put(attribute,o);
    }

    public Map getAttributes(){
        return attributes;
    }


    public String getViewname() {
        return viewname;
    }

    public void setViewname(String viewname) {
        this.viewname = viewname;
    }

}
