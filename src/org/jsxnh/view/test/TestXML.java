package org.jsxnh.view.test;

import com.jsxnh.config.ServerConfig;

public class TestXML {

    public static void main(String[] args){
        ServerConfig serverConfig = new ServerConfig("web1.xml");
        System.out.println(serverConfig);
    }

}
