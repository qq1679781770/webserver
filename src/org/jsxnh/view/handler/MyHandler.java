package org.jsxnh.view.handler;

import com.jsxnh.annotation.RequestBody;
import com.jsxnh.annotation.RequestMapping;
import com.jsxnh.annotation.Requestparam;
import com.jsxnh.annotation.ResponseBody;
import com.jsxnh.http.HttpMethod;
import com.jsxnh.http.HttpRequest;
import com.jsxnh.web.Session;
import org.json.JSONObject;
import org.jsxnh.view.pojo.User;

public class MyHandler {

    @RequestMapping("/")
    public String index(){
        return "html/index.html";
    }

    @ResponseBody
    @RequestMapping(value = "/register",produce = "text/plain")
    public String register(@Requestparam("userid")String username,@Requestparam("password") String password){
        System.out.println("用户名:"+username);
        System.out.println("密码:"+password);
        return "yes";
    }

    @ResponseBody
    @RequestMapping(value = "/login",method = HttpMethod.POST,produce = "application/json")
    public String login(@RequestBody String str, HttpRequest request){
        System.out.println(str);
        JSONObject jsonObject = new JSONObject(str);
        User user = new User();
        user.setUsername(jsonObject.getString("username"));
        user.setPassword(jsonObject.getString("password"));
        Session session = request.getSession();
        session.addAttribute("user",user);
        JSONObject res = new JSONObject();
        res.put("res","success");
        return res.toString();
    }



}
