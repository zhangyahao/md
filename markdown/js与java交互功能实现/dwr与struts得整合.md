Struts2与DWR无法在项目中共存的解决方案

在开发web项目中，有些时候Struts2无法与DWR共存于项目中，
其实原因很简单，就是Struts2的过滤器和DWR的过滤器产生冲突，
web server无法准确判断准确的地址，

打开web.xml会发现，我们配置了如下的信息

首先是Struts2的过滤器
```aidl
<filter>
     <filter-name>struts2</filter-name>
     <filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
   </filter>
   <filter-mapping>
     <filter-name>struts2</filter-name>
     <url-pattern>/*</url-pattern>
   </filter-mapping>
```

 
 可以看到，在url-pattern中，我们将web项目下的所有路径都交给了struts2来处理，
 
然而，我们又配置了DWR的过滤器
```
<servlet>
    <servlet-name>dwr-invoker</servlet-name>
    <servlet-class>org.directwebremoting.servlet.DwrServlet</servlet-class>
    
    <init-param>
     <param-name>config</param-name>
     <param-value>WEB-INF/dwr.xml</param-value>
    </init-param>
    
    <init-param>
      <param-name>debug</param-name>
      <param-value>true</param-value>
    </init-param>

 <init-param>
  <param-name>crossDomainSessionSecurity</param-name>
  <param-value>false</param-value>
 </init-param>

    <load-on-startup>1</load-on-startup>
  </servlet>
  
  <servlet-mapping>
   <servlet-name>dwr-invoker</servlet-name>
   <url-pattern>/dwr/*</url-pattern>
  </servlet-mapping>

```

虽然，这里我们配置了url-pattern   /dwr/*下的所有路径归DWR，

但是，在前方已经配置了将web项目下的所有路径交给Struts2，

因此，这就是产生冲突的根本原因。


#### 1. 第一种解决方案：

在Struts2所处理的路径中，分出一部分让DWR处理：
在Struts2中有个属性叫excludePattern，用来配置Struts2不处理的路径，
在Struts.xml中添加配置：
<constant name="struts.action.excludePattern" value="/dwr/test/*"/>
让/dwr/test/*下的内容不再受Struts2处理。


#### 2. 第二种解决方案：
把Struts2的过滤路径配置为*.action
这样Struts2就只会处理后缀为action的资源，

#### 3. 第三种解决方案：
在struts.xml中配置后缀：
<constant name="struts.action.extension" value="action,go,do" /> 
这样Struts2就只会处理action,go,do结尾的资源
