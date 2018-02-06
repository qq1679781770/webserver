package com.jsxnh.util;

import com.jsxnh.config.ServerConfig;
import com.jsxnh.exception.ServerConfigNotFoundException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;

public class XMLUtil {

    public static void parse(ServerConfig serverConfig) throws ServerConfigNotFoundException {

        File file = null;
        if(serverConfig.getWebxmlfile()!=null){
            file = serverConfig.getWebxmlfile();
        }else if(serverConfig.getWebxmlfilename()!=null){
            file = new File(serverConfig.getWebxmlfilename());
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            NodeList portlist = document.getElementsByTagName("port");
            if(portlist.getLength()!=0){
                serverConfig.setPort(Integer.parseInt(portlist.item(0).getTextContent()));
            }
            NodeList charsetlist = document.getElementsByTagName("charset");
            if(charsetlist.getLength()!=0){
                serverConfig.setCharset(charsetlist.item(0).getTextContent());
            }
            NodeList handlerlist = document.getElementsByTagName("handler");
            for(int i=0;i<handlerlist.getLength();i++){
                serverConfig.addRouter(handlerlist.item(i).getTextContent().trim());
            }
            NodeList staticlist = document.getElementsByTagName("static");
            for(int i=0;i<staticlist.getLength();i++){
                serverConfig.getRouter().addRouter(staticlist.item(i).getTextContent().trim());
            }
            NodeList interlist = document.getElementsByTagName("interceptor");
            for(int i=0;i<interlist.getLength();i++){
                NodeList childs = interlist.item(i).getChildNodes();
                String name = "";
                String value = "";
                for(int j=0;j<childs.getLength();j++){
                    if(childs.item(j).getNodeName().equals("name")){
                        name = childs.item(j).getTextContent().trim();
                    }else if(childs.item(j).getNodeName().equals("class")){
                        value = childs.item(j).getTextContent().trim();
                    }
                }
                serverConfig.addInterceptor(name,value);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(file.getAbsolutePath());
            throw new ServerConfigNotFoundException();

        }


    }

}
