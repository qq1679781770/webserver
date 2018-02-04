package com.jsxnh.http;

import com.jsxnh.http.abs.Context;
import com.jsxnh.util.LoggerUtil;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class HttpHandlerRunable implements Runnable{
    public static Logger logger = LoggerUtil.getLogger(HttpHandlerRunable.class);

    private SocketChannel channel;
    private SelectionKey key;
    private Context context;

    public HttpHandlerRunable(SocketChannel channel,SelectionKey key){
        this.channel = channel;
        this.key = key;
        context = new ServerContext();
    }

    @Override
    public void run() {

        context.setContext(channel,key);
        HttpHandler.init(context);
    }
}
