package com.jsxnh.http.handler;

import com.jsxnh.http.HttpResponse;
import com.jsxnh.http.abs.Context;

import java.util.Date;

public class NotFoundHandler {

    public static void sendResponse(Context context){
        String firstheader = "HTTP/1.1 404 Not Found";
        String headerparam = "";
        headerparam += "Content_Length:0\r\n";
        headerparam += "Date:"+new Date()+"\r\n\r\n";
        ((HttpResponse)context.getResponse()).sendResponse(firstheader+"\r\n"+headerparam);
    }

}
