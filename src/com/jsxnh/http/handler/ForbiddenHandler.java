package com.jsxnh.http.handler;

import com.jsxnh.http.HttpResponse;
import com.jsxnh.http.abs.Context;

public class ForbiddenHandler {
    public static void sendResponse(Context context){
        HttpResponse response = (HttpResponse)context.getResponse();
        response.setContent_type(null);
        response.setStatuscode("403 ForBidden");
        response.sendResponse();
    }
}
