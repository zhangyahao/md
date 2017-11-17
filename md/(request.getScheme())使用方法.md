```$xslt
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    <base href="<%=basePath%>">
 ```
 request.getSchema()可以返回当前页面使用的协议，http 或是 https;
 request.getServerName()可以返回当前页面所在的服务器的名字;
 request.getServerPort()可以返回当前页面所在的服务器使用的端口,就是80;
 request.getContextPath()可以返回当前页面所在的应用的名字;
 
 
