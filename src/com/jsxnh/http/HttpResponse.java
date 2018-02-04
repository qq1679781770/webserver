package com.jsxnh.http;

import com.jsxnh.http.abs.Context;
import com.jsxnh.http.api.Response;

import java.nio.channels.SelectionKey;

public class HttpResponse implements Response{

    private SelectionKey key;
    private Context context;

    public HttpResponse(SelectionKey key){
        this.key = key;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
}
