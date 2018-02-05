package com.jsxnh.http;

import com.jsxnh.http.abs.Context;
import com.jsxnh.util.LoggerUtil;

import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class HttpHandlerRunable{
    public static Logger logger = LoggerUtil.getLogger(HttpHandlerRunable.class);

    private SocketChannel channel;
    private SelectionKey key;
    private Context context;

    public HttpHandlerRunable(SocketChannel channel,SelectionKey key){
        this.channel = channel;
        this.key = key;
        context = new ServerContext();
    }


    public  void doRead(){

        context.setContext(channel,key);
        byte[] b = ((HttpRequest)context.getRequest()).doread();
        if(b==null||b.length==0){
            return;
        }

        new Thread(){
            @Override
            public void run(){
                System.out.println("new Thread run");
                HttpHandler.init(context);
            }
        }.start();

    }
}
