package org.jsxnh.view.handler;

import com.jsxnh.annotation.RequestMapping;

public class MyHandler {

    @RequestMapping("/")
    public String index(){
        return "html/index.html";
    }

}
