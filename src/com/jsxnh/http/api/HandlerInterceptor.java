package com.jsxnh.http.api;

import com.jsxnh.http.HttpRequest;
import com.jsxnh.http.HttpResponse;

public interface HandlerInterceptor {
    public boolean preHandler(HttpRequest request, HttpResponse response);
}
