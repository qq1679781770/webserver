package com.jsxnh.http;

import com.jsxnh.http.abs.Context;
import com.jsxnh.util.LoggerUtil;

import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HttpHandlerRunable{
    public static Logger logger = LoggerUtil.getLogger(HttpHandlerRunable.class);

    public static Map<SocketChannel,ServerContext> channelServerContextMap = new HashMap<>();

    private SocketChannel channel;
    private SelectionKey key;
    private Context context;

    public HttpHandlerRunable(SocketChannel channel,SelectionKey key){
        this.channel = channel;
        this.key = key;
        if(channelServerContextMap.get(channel)!=null){
            context = channelServerContextMap.get(channel);
        }else {
            context = new ServerContext();
            context.setContext(channel,key);
            channelServerContextMap.put(channel,(ServerContext) context);
        }
    }


    public  void doRead(){


        byte[] b = ((HttpRequest)context.getRequest()).doread();
        if(b==null||b.length==0){
            return;
        }
        ((HttpRequest)context.getRequest()).init();
        if(((HttpRequest) context.getRequest()).isOk()){
            new Thread(){
                @Override
                public void run(){
                    System.out.println("new Thread run");
                    HttpHandler.init(context);
                }
            }.start();
        }
    }
}
