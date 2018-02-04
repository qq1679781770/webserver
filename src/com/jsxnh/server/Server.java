package com.jsxnh.server;

import com.jsxnh.config.ServerConfig;
import com.jsxnh.http.HttpHandlerRunable;
import com.jsxnh.util.LoggerUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static ServerConfig serverConfig;
    public Selector selector;
    public static final Logger logger = LoggerUtil.getLogger(Server.class);


    public Server(){

    }

    public Server(ServerConfig serverConfig){
        this.serverConfig = serverConfig;
    }

    public void create(){
        if(serverConfig==null){
            serverConfig = new ServerConfig();
        }
        try {
            selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.socket().bind(new InetSocketAddress(serverConfig.getPort()));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("server listen on port:"+serverConfig.getPort());
        } catch (IOException e) {
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        }

    }

    public void listen(){
        while (selector.isOpen()){
            try{
                int readyChannels = selector.select();
                if(readyChannels == 0)
                    continue;
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()){
                    try {
                        SelectionKey key = iterator.next();
                        SocketChannel channel = null;
                        if (key.isAcceptable()) {
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            try {
                                channel = server.accept();
                                if (channel != null) {
                                    channel.configureBlocking(false);
                                    channel.register(selector, SelectionKey.OP_READ);
                                }
                            } catch (IOException e) {
                                logger.log(Level.SEVERE, "accept connect error", e);
                                if (channel != null) {
                                    key.cancel();
                                    channel.close();
                                }
                            }
                        } else if (key.isReadable()) {
                            new Thread(new HttpHandlerRunable((SocketChannel)key.channel(),key)).start();
                        }
                    } catch (IOException e) {
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "", e);
                    } finally {
                        iterator.remove();
                    }
                }
            }catch (Exception e){
                logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
            }
        }
    }


    public void start(){
        create();
        new Thread(){
            @Override
            public void run() {
                Server.this.listen();
            }
        }.start();
    }
}
