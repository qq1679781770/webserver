package com.jsxnh.exception;

public class SameRouterException extends Exception{

    public SameRouterException(String url){
        super("has the url:"+url);
    }

}
