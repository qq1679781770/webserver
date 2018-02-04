package com.jsxnh.util;

import java.util.HashMap;

public class ContentTypeUtil {

    public static HashMap<String,String> map = new HashMap<>();
    static {

        map.put(".bmp","application/x-bmp");
        map.put(".gif","image/gif");
        map.put(".html","text/html");
        map.put(".jpe","image/jpeg");
        map.put(".jpeg","image/jpeg");
        map.put(".jpg","application/x-jpg");
        map.put(".css","text/css");
        map.put(".js","application/x-javascript");
        map.put(".png","image/png");

    }

    public String getContent_Type(String filesuffix){
        if(map.containsKey(filesuffix))
            return map.get(filesuffix);
        return null;
    }

}
