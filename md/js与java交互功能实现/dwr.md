DWR使用步骤：


1.导入jar包
commons-logging-1.0.4.jar
dwr3.0.1.jar


2.在web.xml中配置servlet
```aidl
<servlet>
	<display-name>DWR Servlet</display-name>
	<servlet-name>dwr-invoker</servlet-name>
	<servlet-class>org.directwebremoting.servlet.DwrServlet</servlet-class>
	<init-param>
		<param-name>debug</param-name>
		<param-value>true</param-value>
	</init-param>
	<init-param>
		<param-name>activeReverseAjaxEnabled</param-name>
		<param-value>true</param-value>
	</init-param>
	<init-param>
		<param-name>initApplicationScopeCreatorsAtStartup</param-name>
		<param-value>true</param-value>
	</init-param>
	<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
	<servlet-name>dwr-invoker</servlet-name>
	<url-pattern>/dwr/*</url-pattern>
</servlet-mapping>
```


3.编写Java类
```aidl
public class Hello {
	
	public int sum(int a,int b){
		return a+b;
	}
	
	public String say(){
		return "中华人民共和国万岁，世界人民大团结万岁";
	}
	
}


```


4.DWR配置文件

在web.xml旁创建dwr.xml:
```
<!DOCTYPE dwr PUBLIC
    "-//GetAhead Limited//DTD Direct Web Remoting 3.0//EN"
    "http://getahead.org/dwr/dwr30.dtd">

<dwr>
  <allow>
   //这是我们在js中使用得脚本得名字
    <create creator="new" javascript="hello">
    //js中加奥本所对应得类得位置  value是类所在得包名以及类得名字
      <param name="class" value="demo.ajax.Hello"/>
    </create>
    
  </allow>
</dwr>
```


5.在页面中引入DWR
```aidl
<script src='<%=path %>/dwr/engine.js'></script>
<script src='<%=path %>/dwr/util.js'></script>
// hello.js是自己在配置中加入的
<script src='<%=path %>/dwr/interface/hello.js'></script>

```

6.在页面的script标签中使用
//访问无参数方法
hello.say(function(str){
		alert(str);
	});
//调用有参数方法
hello.sum(3,6,function(str){
			alert(str);
	});
