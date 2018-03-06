package com.jsxnh.http;

import com.jsxnh.config.ServerConfig;
import com.jsxnh.http.abs.Context;
import com.jsxnh.http.api.Request;
import com.jsxnh.util.ByteUtil;
import com.jsxnh.util.FileUtil;
import com.jsxnh.util.LoggerUtil;
import com.jsxnh.web.Cookie;
import com.jsxnh.web.Session;

import java.io.*;
import java.nio.*;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpRequest implements Request{

    private String uri;
    private String url;
    private String context_type;
    private HttpMethod method;
    private String charset;
    private String protocol;
    private Map<String,Object> attributes = new HashMap<>();

    private Map<String,String> headerParams = new HashMap<>();
    private Map<String,File[]> files = new HashMap<>();
    private InputStream inputStream;
    private Cookie cookie;
    private SocketChannel channel;
    private String referer;
    private Context context;
    private byte[] bytes;
    private String boundary;
    private int content_length;
    private boolean isok;

    private byte[] requestbody;

    public static Logger logger = LoggerUtil.getLogger(HttpRequest.class);

    public HttpRequest(SocketChannel sc){
        this.channel = sc;
        isok = false;
    }

    public synchronized byte[] doread(){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int size = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((size = channel.read(buffer)) > 0) {
                buffer.flip();
                bytes = new byte[size];
                buffer.get(bytes);
                baos.write(bytes);
                buffer.clear();
            }
            bytes = baos.toByteArray();
        }catch (Exception e){
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        }
        return bytes;
    }

    public void init(){
        try {
            String requeststr = new String(bytes);
            int totalsize = bytes.length;
            if(requeststr.split("\r\n")[0].split(" ").length==3){
                initSchema(requeststr.split("\r\n")[0]);
                String[] requestheaders = requeststr.substring(0,requeststr.indexOf("\r\n\r\n")).split("\r\n");
                for(int i=1;i<requestheaders.length;i++){
                    String quaramkey = requestheaders[i].substring(0,requestheaders[i].indexOf(":"));
                    if(quaramkey.equals("Content-Type")){
                        attributes.put(requestheaders[i].substring(0,requestheaders[i].indexOf(":")),requestheaders[i].substring(requestheaders[i].indexOf(":")+1));
                        context_type = requestheaders[i].split(";")[0].substring(requestheaders[i].indexOf(":")+1).trim();
                        String key  = requestheaders[i].split(";")[1].substring(0,requestheaders[i].split(";")[1].indexOf("=")).trim();
                        if(key.equals("charset")){
                            charset = requestheaders[i].split(";")[1].substring(requestheaders[i].split(";")[1].indexOf("=")+1);
                        }else if(key.equals("boundary")){
                            boundary = requestheaders[i].split(";")[1].substring(requestheaders[i].split(";")[1].indexOf("=")+1).trim();
                        }
                    }else if(quaramkey.equals("Cookie")){
                        String cookies = requestheaders[i].substring(requestheaders[i].indexOf(":")+1);
                        cookies = cookies.trim();
                        cookie = new Cookie(cookies);
                    }else if(quaramkey.equals("Content-Length")){
                        content_length = Integer.parseInt(requestheaders[i].substring(requestheaders[i].indexOf(":")+1).trim());
                    } else{
                        attributes.put(requestheaders[i].substring(0,requestheaders[i].indexOf(":")),requestheaders[i].substring(requestheaders[i].indexOf(":")+1));
                    }
                }


                if(context_type!=null&&context_type.equals("multipart/form-data")){
                    String requestheader = requeststr.substring(0,requeststr.indexOf("\r\n\r\n")+"\r\n\r\n".length());
                    int requestheadersize = requestheader.getBytes().length;
                    if(totalsize-requestheadersize !=content_length){
                        String res = "HTTP/1.1 100 Continue\r\n\r\n";
                        byte[] b = res.getBytes();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(b.length);
                        byteBuffer.put(b);
                        byteBuffer.flip();
                        channel.write(byteBuffer);
                    }else{
                        isok = true;
                    }
                }else{
                    isok = true;
                }
                requestbody = requeststr.substring(requeststr.indexOf("\r\n\r\n")+"\r\n\r\n".length()).getBytes();
            }else{
                if(context_type!=null&&context_type.equals("multipart/form-data")){
                    requestbody = ByteUtil.merge(requestbody,bytes);
                    if(requestbody.length==content_length){
                        isok = true;
                    }else{
                        String res = "HTTP/1.1 100 Continue\r\n\r\n";
                        byte[] b = res.getBytes();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(b.length);
                        byteBuffer.put(b);
                        byteBuffer.flip();
                        channel.write(byteBuffer);
                    }
                }
            }
        }catch (Exception e){
            logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
        }

    }

    public void initBody(){
        if(requestbody==null||requestbody.length==0){
            return;
        }
        if(context_type.equals("application/x-www-form-urlencoded")){
            String[] params = new String(requestbody).split("&");
            for(String s:params){
                attributes.put(s.substring(0,s.indexOf("=")),s.substring(s.indexOf("=")+1));
            }
        }else if(context_type.equals("multipart/form-data")){
            List<byte[]> bodys = new LinkedList<byte[]>();
            requestbody = ByteUtil.subBytes(requestbody,0,requestbody.length-("\r\n--"+boundary+"--").getBytes().length);
            byte[] bound = ("--"+boundary).getBytes();
            List l = ByteUtil.ByteIndexof(requestbody,bound);
            if(l.size()==1||l.size()==0){
                bodys.add(requestbody);
            }else{
                for(int i=0;i<l.size()-1;i++){
                    bodys.add(ByteUtil.subBytes(requestbody,(Integer)l.get(i),(Integer)l.get(i+1)-(Integer)l.get(i)));
                }
                bodys.add(ByteUtil.subBytes(requestbody,(Integer)l.get(l.size()-1),requestbody.length-(Integer)l.get(l.size()-1)));
            }
            for(byte[] b:bodys){
                String s = new String(b);
                String key = null;
                String type = null;
                String filename = null;
                String sh = s.substring(0,s.indexOf("\r\n\r\n"));
                String[] params = sh.split("\r\n");
                for(String sh_:params){
                    sh_ = sh_.trim();
                    if(sh_.indexOf("Content-Disposition")!=-1){
                        String[] shh = sh_.split(";");
                        key = shh[1].substring(shh[1].indexOf("=")+2,shh[1].length()-1);
                        if(shh.length==3){
                            filename = shh[2].substring(shh[2].indexOf("=")+2,shh[2].length()-1);
                        }
                    }else if(sh_.indexOf("Content-Type")!=-1){
                        type = sh_.substring(sh_.indexOf(":")+1).trim();
                    }
                }
                if(type==null){
                    String value = s.substring(s.indexOf("\r\n\r\n")+"\r\n\r\n".length()).trim();
                    attributes.put(key,value);
                }else{
                    int size = s.substring(0,s.indexOf("\r\n\r\n")+"\r\n\r\n".length()).getBytes().length;
                    byte[] filebyte = ByteUtil.subBytes(b,size,b.length-size);
                    if(filename.equals("")||filename==null)
                        continue;
                    File file = new File(filename);
                    try {
                        FileUtil.writeTofile(file,filebyte);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE,LoggerUtil.recordStackTraceMsg(e));
                    }
                    if(files.get(key)!=null){
                        File[] files_ = files.get(key);
                        File[] newfiles = new File[files_.length+1];
                        System.arraycopy(files_,0,newfiles,0,files_.length);
                        newfiles[files_.length] = file;
                        files.put(key,newfiles);
                    }else{
                        files.put(key,new File[]{file});
                    }
                }

            }
        }

    }

    public void  clearFile(){
        for(String key:files.keySet()){
            File[] fs = files.get(key);
            for(File f:fs){
                FileUtil.deleteFile(f);
            }
        }
    }

    public Session getSession(){
        if(cookie==null){
            cookie = new Cookie();
        }
        if(cookie.getAttriute(Cookie.SESSION) == null){
            Session session = new Session();
            session.setId(Session.generatorId());
            Session.addSession(session.getId(),session);
            cookie.addAttribue(Cookie.SESSION,session.getId());
            return session;
        }

        String id = cookie.getAttriute(Cookie.SESSION);
        Session session = Session.getSession(id);
        if(session==null){
            session = new Session();
            session.setId(Session.generatorId());
            Session.addSession(session.getId(),session);
            cookie.addAttribue(Cookie.SESSION,session.getId());
        }
        return session;
    }

    private void initSchema(String headerfirst){
        //System.out.println(headerfirst);
        String s[] = headerfirst.split(" ");
        String method = s[0];
        if(method.equals("GET")){
            this.method = HttpMethod.GET;
        }else if(method.equals("POST")){
            this.method = HttpMethod.POST;
        }
        this.protocol = s[2];
        this.url = s[1];
        if(s[1].indexOf("?")==-1){
            this.uri = s[1];
        }else{
            this.uri = s[1].substring(0,s[1].indexOf("?"));
            String paramstr = s[1].substring(s[1].indexOf("?")+1);
            String[] params = paramstr.split("&");
            for(String p:params){
                headerParams.put(p.substring(0,p.indexOf("=")),p.substring(p.indexOf("=")+1));
                attributes.put(p.substring(0,p.indexOf("=")),p.substring(p.indexOf("=")+1));
            }
        }
    }


    @Override
    public Object getAttribute(String key) {
        if(isContain(key)){
            return attributes.get(key);
        }else{
            return null;
        }
    }

    @Override
    public boolean isContain(String attribute) {
        for(String key:attributes.keySet()){
            if(key.equals(attribute)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }



    @Override
    public Cookie getCookie() {
        return cookie;
    }

    @Override
    public Boolean isCookie() {
        return cookie!=null;
    }

    @Override
    public ServerConfig getServerConfig() {
        return context.getServerConfig();
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    public byte[] getRequestbody() {
        return requestbody;
    }

    public Map<String, String> getHeaderParams() {
        return headerParams;
    }

    public boolean isOk(){
        return isok;
    }

    public File[] getFiles(String key){
        return files.get(key);
    }
}
