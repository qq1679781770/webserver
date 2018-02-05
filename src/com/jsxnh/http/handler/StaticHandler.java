package com.jsxnh.http.handler;

import com.jsxnh.http.HttpRequest;
import com.jsxnh.http.HttpResponse;

import java.io.File;

public class StaticHandler {

    public void dealStatic(HttpRequest request, HttpResponse response){
        File file = new File(request.getUri().substring(1));
        response.sendResponse(file);
    }
}
