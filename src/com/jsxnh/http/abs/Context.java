package com.jsxnh.http.abs;

import com.jsxnh.config.ServerConfig;
import com.jsxnh.http.HttpRequest;
import com.jsxnh.http.HttpResponse;
import com.jsxnh.http.api.Request;
import com.jsxnh.http.api.Response;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public abstract class Context {
    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    public Request request;
    public Response response;
    public ServerConfig serverConfig;

    public void setContext(SocketChannel sc, SelectionKey k){

    }

    public ServerConfig getServerConfig(){
        return serverConfig;
    }

}
