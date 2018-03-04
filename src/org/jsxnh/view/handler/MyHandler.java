package org.jsxnh.view.handler;

import com.jsxnh.annotation.*;
import com.jsxnh.http.HttpMethod;
import com.jsxnh.http.HttpRequest;
import com.jsxnh.web.ModelAndView;
import com.jsxnh.web.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsxnh.view.pojo.User;
import org.jsxnh.view.service.KyService;

@Interceptor("inter")
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
        return "res";
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
        if(session.getAttribute("user")!=null){
            User u = (User)session.getAttribute("user");
            System.out.println("session content:"+u.getUsername()+"==="+u.getPassword());
        }
        session.addAttribute("user",user);
        JSONObject res = new JSONObject();
        res.put("res","success");
        return res.toString();
    }


    @RequestMapping(value = "/testModel",method = HttpMethod.GET)
    public ModelAndView testModelAndView(){

        ModelAndView modelAndView = new ModelAndView("html/testmodel.html");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","csb数独书");
        jsonObject.put("asa",false);
        modelAndView.addObject("jsonobject",jsonObject.toString());
        modelAndView.addObject("str","str");
        modelAndView.addObject("int",1);
        modelAndView.addObject("bool",false);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        jsonArray.put(jsonObject);
        modelAndView.addObject("jsonarray",jsonArray.toString());
        return modelAndView;
    }


    @RequestMapping(value = "/ky",method = HttpMethod.GET)
    public ModelAndView findkyQuestions(){
        ModelAndView modelAndView = new ModelAndView("html/ky.html");
        KyService kyService= new KyService();
        modelAndView.addObject("questions",kyService.getQuestions().toString());
        return  modelAndView;
    }

    @RequestMapping(value = "/form",method = HttpMethod.GET)
    public String testForm(){
        return "html/form.html";
    }

    @ResponseBody
    @RequestMapping(value = "/fileupload",method = HttpMethod.POST,produce = "text/plain")
    public String fileupload(HttpRequest request){
        System.out.println(request.getAttribute("username"));
        System.out.println(request.getAttribute("password"));
        System.out.println(request.getFiles("file1").length);
        System.out.println(request.getFiles("file2").length);
        return "res";
    }

}
