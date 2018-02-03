package com.jsxnh.exception;

public class ServerConfigNotFoundException extends Exception{

    public ServerConfigNotFoundException(){
        super("server config file not  found");
    }

    public ServerConfigNotFoundException(String msg){
        super(msg);
    }

}
