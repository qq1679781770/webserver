package org.jsxnh.view.test;

import com.jsxnh.config.ServerConfig;
import com.jsxnh.server.Server;

public class TestRquest {

    public static void main(String[] args){
        ServerConfig serverConfig = new ServerConfig("web.xml");
        new Server(serverConfig).start();
    }
}
