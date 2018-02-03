package com.jsxnh.util;

import com.jsxnh.config.ServerConfig;
import com.jsxnh.exception.ServerConfigNotFoundException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

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
