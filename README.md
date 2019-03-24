### WebServer
这是一款轻量级的基于Java的Web Application Server

##### 快速上手
----

 - 配置文件
在项目目录下新建web.xml文件

	``` 
	<?xml version="1.0" encoding="utf-8" ?>
	<server>
    	<port>8001</port><!-->监听端口<-->
    	<logpath></logpath><!-->日志目录<-->
    	<mvc>
       	 <handlers>
            	<handler>org.jsxnh.view.handler.MyHandler</handler>
       	 </handlers><!-->url router class<-->
        <interceptors>
            <interceptor>
                <name>inter</name><!-->interceptor name<-->
                <class>org.jsxnh.view.handler.MyInterceptor</class> <!-->intercepter class<-->
            </interceptor>
        </interceptors>
        <statics>
            <static>static</static> <!-->静态文件目录<-->
        </statics>
    	</mvc>
	</server>
	```
 - 启动服务器
 
	``` 
	public class TestRquest {
		public static void main(String[] args){
			ServerConfig serverConfig = new ServerConfig("web.xml");
			new Server(serverConfig).start();
		}
	}
	```

 - Controller
使用Spring MVC的方式，利用注解提供Restful API

	``` 
	@ResponseBody
    @RequestMapping(value = "/login",method = HttpMethod.POST,produce = "application/json")
   	 	public String login(@RequestBody String str, HttpRequest request){    
    }
	```
	
	```
	其中，ResponseBody表示方法返回值作为服务器的返回值
	RequestMapping，value为url的相对值，method为请求方法，produce为返回的Content-type
	RequestBody接收请求参数，方法参数名对应前端的name
	HttpRequest和HttpResponse分别为请求和返回，类似于servlet
	```

 -  Interceptor
	 自定义拦截器类需要继承HandlerInterceptor接口
	 ```
	 public class MyInterceptor implements HandlerInterceptor{
		@Override
		public boolean preHandler(HttpRequest request, HttpResponse response) {
		  //返回false为拦截
		}
	}
	 ```
	 
##### 其他

 - 支持Model And View
 - 支持Cookie And Session
 - 支持文件传输