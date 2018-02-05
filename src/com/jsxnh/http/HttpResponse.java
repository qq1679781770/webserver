package com.jsxnh.http;

import com.jsxnh.http.abs.Context;
import com.jsxnh.http.api.Response;
import com.jsxnh.util.ByteUtil;
import com.jsxnh.util.ContentTypeUtil;
import com.jsxnh.util.LoggerUtil;
import com.jsxnh.web.Cookie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpResponse implements Response{

    private SelectionKey key;
    private Context context;



    private String content_type;
    private String charset;
    private Date date;
    private String connection;
    private String content_length;
    private Cookie cookie;
    private String statuscode;

    public static Logger logger = LoggerUtil.getLogger(HttpResponse.class);


    public HttpResponse(SelectionKey key){
        this.key = key;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }


    public Context getContext() {
        return context;
    }

    public String getContent_type() {
        return content_type;
    }

    public String getCharset() {
        return charset;
    }

    public Date getDate() {
        return date;
    }

    public String getConnection() {
        return connection;
    }

    public String getContent_length() {
        return content_length;
    }

    public Cookie getCookie() {
        return cookie;
    }

    @Override
    public String statuscode() {
        return null;
    }


    public void  sendResponse(String str){

        byte[] bytes = str.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        SocketChannel channel = (SocketChannel) key.channel();
        byteBuffer.flip();
        try {
            channel.write(byteBuffer);
            channel.register(key.selector(),SelectionKey.OP_WRITE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendResponse(File file){
        String filesuffix = file.getName().substring(file.getName().indexOf("."));
        String context_type = ContentTypeUtil.getContent_Type(filesuffix);

        try {
            FileInputStream inputStream = new FileInputStream(file);
            int length = inputStream.available();
            String header = "HTTP/1.1 200 OK\r\n"+
                    "Content_Type:"+context_type+"\r\n"+
                    //"Content_Length:"+String.valueOf(length)+"\r\n"+
                    "Date:"+new Date()+"\r\n\r\n";

            ByteBuffer headerbuffer = ByteBuffer.allocate(header.getBytes().length);
            byte[] bytes = new byte[1024];
            int l = 0 ;
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            SocketChannel socketChannel = (SocketChannel)key.channel();
            headerbuffer.put(header.getBytes());
            headerbuffer.flip();
            socketChannel.write(headerbuffer);
            while ((l=inputStream.read(bytes,0,bytes.length))!=-1){
                byteBuffer.put(ByteUtil.subBytes(bytes,0,l));
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                byteBuffer.clear();

            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        } catch (IOException e) {
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        }


    }
}
