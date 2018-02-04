package com.jsxnh.web;

import java.lang.reflect.Method;

public class Controller{

    private String classname;
    private Method method;

    public Controller(String classname, Method method) {
        this.classname = classname;
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public String getClassname() {
        return classname;
    }
}
