package org.jsxnh.view.handler;

import com.jsxnh.http.HttpRequest;
import com.jsxnh.http.HttpResponse;
import com.jsxnh.http.api.HandlerInterceptor;
import com.jsxnh.web.Session;

public class MyInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandler(HttpRequest request, HttpResponse response) {
        Session session = request.getSession();
        if(session.getAttribute("user")==null){
            return false;
        }
        return true;
    }
}
