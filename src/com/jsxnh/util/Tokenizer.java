package com.jsxnh.util;

import java.util.HashMap;
import java.util.Map;

public class Tokenizer {

    public static final String firstexp = "${";
    public static final String lastexp = "}";
    private String string;
    private Map<String,Object> map = new HashMap<>();

    public Tokenizer(String s,Map m){
        this.string = s;
        this.map = m;
    }

    public String parse(){
        StringBuilder stringBuilder = new StringBuilder();
        while(!string.equals("")||string!=null){
            int index = string.indexOf(firstexp);
            if(index==-1||index==string.length()-2)
                break;
            stringBuilder.append(string.substring(0,index));
            string = string.substring(index);
            index = 0;
            int index_ = string.indexOf(lastexp);
            if(index_==-1)
                break;
            String attr = string.substring(2,index_).trim();
            if(attr.equals("")){

            }else{
                stringBuilder.append(map.get(attr));
            }
            if(index_==string.length()-1){
                string = null;
            }else {
                string = string.substring(index_+1);
            }
        }
        if(string!=null){
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }
}
