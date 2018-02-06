package com.jsxnh.exception;



public class NotFoundInteceptor extends Exception{

    public NotFoundInteceptor(String classname){
        super("not found inteceptor:"+classname);
    }
}
