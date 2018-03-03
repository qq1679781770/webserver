package com.jsxnh.http;

import com.jsxnh.http.abs.Context;
import com.jsxnh.http.api.Response;
import com.jsxnh.util.ByteUtil;
import com.jsxnh.util.ContentTypeUtil;
import com.jsxnh.util.LoggerUtil;
import com.jsxnh.util.Tokenizer;
import com.jsxnh.web.Cookie;
import com.jsxnh.web.ModelAndView;

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
    private String connection = "keep-alive";
    private String content_length;
    private Cookie cookie;
    private SocketChannel socketChannel;



    private String statuscode = "200 OK";

    public static Logger logger = LoggerUtil.getLogger(HttpResponse.class);


    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public HttpResponse(SelectionKey key){
        this.key = key;
        this.socketChannel = (SocketChannel)key.channel();
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
        return statuscode;
    }


    private String getHeader(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 "+statuscode+"\r\n");
        if(content_type!=null){
            stringBuilder.append("Content_Type:"+content_type);
            if(charset!=null){
                stringBuilder.append(";charset="+charset);
            }
            stringBuilder.append("\r\n");
        }
        stringBuilder.append("Connection:"+connection+"\r\n");
        stringBuilder.append("Date:"+new Date()+"\r\n");
        stringBuilder.append("\r\n");
        return stringBuilder.toString();
    }

    public void sendResponse(){
        byte[] bytes = getHeader().getBytes();
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

    public void  sendResponse(String str){

        byte[] bytes = str.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        SocketChannel channel = (SocketChannel) key.channel();
        byteBuffer.flip();
        try {
            channel.write(byteBuffer);
            //channel.register(key.selector(),SelectionKey.OP_WRITE);

        } catch (IOException e) {
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        }
        close();
    }


    public void sendResponse(File file){
        String filesuffix = file.getName().substring(file.getName().indexOf("."));
        String context_type = ContentTypeUtil.getContent_Type(filesuffix);

        try {
            FileInputStream inputStream = new FileInputStream(file);
            int length = inputStream.available();
            String header = "HTTP/1.1 200 OK\r\n"+
                    "Content_Type:"+context_type+";charset=utf-8\r\n"+
                    "Content_Length:"+String.valueOf(length)+"\r\n"+
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
        close();
    }


    public void sendResponse(ModelAndView modelAndView){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(modelAndView.getViewname())));
            String s;
            while ((s=reader.readLine())!=null){
                stringBuilder.append(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Tokenizer tokenizer = new Tokenizer(stringBuilder.toString(),modelAndView.getAttributes());
        content_type = "text/html";
        String s = tokenizer.parse();
        System.out.println(s);
        sendResponseBody(s);

    }

    public void sendResponseBody(String s){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 200 OK\r\n");
        stringBuilder.append("Content_Type:"+content_type);
        if(charset!=null){
            stringBuilder.append(";charset="+charset);
        }
        stringBuilder.append("\r\n");
        stringBuilder.append("Connection:"+connection+"\r\n");
        stringBuilder.append("Date:"+new Date()+"\r\n");
        if(cookie!=null){
            stringBuilder.append("Set-Cookie:"+cookie.getCookieStr()+"\r\n");
        }else if(context.getRequest().getCookie()!=null){
            stringBuilder.append("Set-Cookie:"+context.getRequest().getCookie().getCookieStr()+"\r\n");
        }
        byte[] b = s.getBytes();
        stringBuilder.append("Content_Length:"+b.length+"\r\n");
        stringBuilder.append("\r\n");
        String ss = stringBuilder.toString()+s;
        ByteBuffer headerbuffer = ByteBuffer.allocate(ss.getBytes().length);
        headerbuffer.put(ss.getBytes());
        try {
            headerbuffer.flip();
            socketChannel.write(headerbuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        close();
    }

    public void sendResponseView(String view){
        File file = new File(view);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            int length = inputStream.available();
            String header = "HTTP/1.1 200 OK\r\n"+
                    "Content_Type:text/html;charset=utf-8\r\n"+
                    "Content_Length:"+String.valueOf(length)+"\r\n"+
                    "Date:"+new Date()+"\r\n";
            if(cookie!=null){
                header +="Set-Cookie:"+cookie.getCookieStr()+"\r\n";
            }else if(context.getRequest().getCookie()!=null){
                header +="Set-Cookie:"+context.getRequest().getCookie().getCookieStr()+"\r\n";
            }
            header +="\r\n";
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
        close();
    }

    private void close(){

        try {
            socketChannel.shutdownInput();
            socketChannel.close();
            HttpHandlerRunable.channelServerContextMap.remove(socketChannel);
        } catch (IOException e) {
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        }
    }
}
