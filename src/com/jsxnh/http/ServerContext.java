package com.jsxnh.http;

import com.jsxnh.http.abs.Context;
import com.jsxnh.http.api.Request;
import com.jsxnh.http.api.Response;
import com.jsxnh.server.Server;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ServerContext extends Context{

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public SelectionKey getKey() {
        return key;
    }

    private SocketChannel socketChannel;
    private SelectionKey key;


    @Override
    public void setContext(SocketChannel sc,SelectionKey k){
        this.socketChannel = sc;
        this.key = k;
        Request request = new HttpRequest(sc);
        request.setContext(this);
        Response response = new HttpResponse(k);
        response.setContext(this);
        super.request = request;
        super.response = response;
        super.serverConfig = Server.serverConfig;
    }


}
