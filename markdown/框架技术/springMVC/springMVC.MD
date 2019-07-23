### SpringMVC 开源MVC框架，可以快速开发网站

快速搭建入门环境：
```aidl
---- pom.xml ----
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-webmvc</artifactId>
	<version>4.3.13.RELEASE</version>
</dependency>

```
---- spring-mvc-servlet.xml ----

---- web.xml ----
```aidl
<servlet>
	<servlet-name>DispatcherServlet</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<init-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:spring-mvc-servlet.xml</param-value>
	</init-param>
	<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
	<servlet-name>DispatcherServlet</servlet-name>
	<url-pattern>/</url-pattern>
</servlet-mapping>

```


用Servlet+Jsp实现的MVC：浏览器->Servlet->Jsp
Servlet负责接请求、增删改查、给响应，JSP只负责数据的展示，
在这种情况下，如果直接访问JSP，只可能：白屏、乱码、报错，所以这些JSP不应该直接访问。
有些JSP执行之前，需要一些已知数据，没有数据则无法运行，那么这些JSP必须隐藏起来，直接访问会出错。
为了实现更好的MVC，通常我们的jsp页面都放到WEB-INF里面，通过请求转发forward进行访问。
简单来说，我们项目开发时，能藏的JSP，都藏起来，通过forward来访问。
这样可以达到全站MVC的目的。
浏览器->Java->请求转发->JSP


SpringMVC运行过程：
1.SpringMVC绑定了项目根路径，无论访问什么路径都会被劫持到核心分发器
2.判断用户访问的URL是否在SpringMVC中有定义，如果有则使用，如果没有则404


获取request、response、session、application：
HttpServletRequest req=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
也可以将request、response、session声明在参数列表中


@RequestParam 接收请求参数，POST参数与Get参数都可以接收
@PathVariable 把URL中的内容转化为变量值

请求转发	return "xxxx";
重定向		return "redirect:xxxx";
不跳转		返回void