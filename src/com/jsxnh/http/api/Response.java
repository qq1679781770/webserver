package com.jsxnh.http.api;

import com.jsxnh.http.abs.Context;
import com.jsxnh.web.Cookie;

import java.util.Date;

public interface Response {


    public void setContext(Context context);


    public Context getContext();

    public String getContent_type();

    public String getCharset();

    public Date getDate();

    public String getConnection();
    public String getContent_length();

    public Cookie getCookie();
    public String statuscode();

}
