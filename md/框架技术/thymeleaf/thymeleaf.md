####thymeleaf
1.  简介
    thymeleaf 是一个跟 Velocity、FreeMarker 类似的模板引擎，它可以完全替代 JSP 。
    相较与其他的模板引擎，它有如下三个极吸引人的特点：
        1. Thymeleaf 在有网络和无网络的环境下皆可运行即它可以让美工在浏览器查看页面的静态效果，
        也可以让程序员在服务器查看带数据的动态页面效果。
        2. thymeleaf 开箱即用的特性。它提供标准和spring标准两种方言，可以直接套用模板实现JSTL、 OGNL
        表达式效果，避免每天套模板、该jstl、改标签的困扰。同时开发人员也可以扩展和创建自定义的方言。
        3. Thymeleaf 提供spring标准方言和一个与 SpringMVC 完美集成的可选模块，可以快速的实现表单绑定、属性编辑器、国际化等功能。
        4. Thymeleaf最大的特点是通过HTML的标签属性渲染标签内容
2.  使用<br>        
    在页面开始时引入
    ```$xslt
        <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
    ```
3.  配置    
    ```$xslt
        # 配置服务器端口，默认是8080，可以不用配置
        server.port=8080
        # 模板配置
        # 这个开发配置为false，避免改了模板还要重启服务器
        spring.thymeleaf.cache=false
        # 这个是配置模板路径的，默认就是templates，可不用配置
        spring.thymeleaf.prefix=classpath:/templates/
        # 这个可以不配置，检查模板位置
        spring.thymeleaf.check-template-location=true
        # 下面3个不做解释了，可以不配置
        spring.thymeleaf.suffix=.html
        spring.thymeleaf.encoding=UTF-8
        spring.thymeleaf.content-type=text/html
        
        # 模板的模式
        spring.thymeleaf.mode=HTML5
    ```
    
4.  简单的表达式
        
    |语法|名称|描述|作用|
    |:-------:|:------:|:-------:|:-------:|
    |${…}|Variable Expressions|变量表达式|取出上下文变量的值|
    |*{…}|Selection Variable Expressions|选择变量表达式	|取出选择的对象的属性值|
    |#{…}|Message Expressions|消息表达式|使文字消息国际化，I18N|
    |@{…}|Link URL Expressions|链接表达式|用于表示各种超链接地址|
    |~{…}|Fragment Expressions|片段表达式|	引用一段公共的代码片段|	 
    
    
       
5. 语法介绍    
    1. 变量表达式<br>
        类似于el表达式,可以使用 `$`来表示属性
        ```$xslt
            <span th:text="${book.author.name}">  
        ```
    2.  选择（星号）表达式 <br>  
        可以将父类中的属性在子类中直接调用
        ```$xslt
         <div th:object="${man}">  
              ...  
              <span th:text="*{age}">...</span>  
              ...  
            </div>  

        ```
    3. URL表达式 <br>   
        url在其中是用`@`来表示的，修饰url时必须使用标签***`th:href，th:src`***
        1.  绝对路径 也可一放其他的链接地址 `http，https`，必须指定协议名
              ```$xslt
              <a href="details.html" 
                 th:href="@{http://localhost:8080/gtvg/order/details(orderId=${o.id})}">view</a>
              ```
        2.  上下文路径 要以 /开头  
               ```$xslt
                   比如路径为 http://localhost:8080/myapp
                   <a href="/billing-app/showDetails.html">
               ```
        3.  添加参数
            ```aidl
                <a th:href="@{/order/details(id=3)}">
            ```
            上面示例代码，最终将输出为:
            ```aidl
                <a href="/order/details?id=3">
            ```
            也就是说括号内的参数将会和url拼接转义
            
    4. 国际化<br> 
        可以改变页面语言 需要引入配置文件<br> 
        [实现方式](https://blog.csdn.net/u010714901/article/details/51581424)
           
    5. 定义模板  (待修改 好像不对)
        在Thymeleaf 中，我们可以使用`_**th:fragment**_`属性来定义一个模板。
        ```$xslt
        <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
        <html xmlns="http://www.w3.org/1999/xhtml"
              xmlns:th="http://www.thymeleaf.org">
          <body>
            <div th:fragment="copyright">
              © 2016 xxx
            </div>
          </body>
        </html>
        ```
        调用模板使用th:include或者th:replace
        ```$xslt
        <body>
          ...
          <div th:include="footer :: copyright"></div> 
        </body>

        ```
6.  语法
    ```aidl
         #dates: java.util的实用方法。对象:日期格式、组件提取等.
         #calendars: 类似于#日期,但对于java.util。日历对象
         #numbers: 格式化数字对象的实用方法。
         #strings:字符串对象的实用方法:包含startsWith,将/附加等。
         #objects: 实用方法的对象。
         #bools: 布尔评价的实用方法。
         #arrays: 数组的实用方法。
         #lists: list集合。
         #sets:set集合。
         #maps: map集合。
         #aggregates: 实用程序方法用于创建聚集在数组或集合.
         #messages: 实用程序方法获取外部信息内部变量表达式,以同样的方式,因为他们将获得使用# {…}语法
         #ids: 实用程序方法来处理可能重复的id属性(例如,由于迭代)。
         #httpServletRequest:用于web应用中获取request请求的参数
         #session:用于web应用中获取session的参数
    ```
    
7. 常用属性
    ```aidl
    h:action      表单提交的地址    　　　       <form action="subscribe.html" th:action="@{/subscribe}">
    th:each        属性赋值    　　　　　　　   　 <tr th:each="user,userStat:${users}">
    th:field       常用于表单字段绑定             <input type="text" value="" th:field="*{username}"></input>
    th:href        链接地址    　　　　　　　    　<a th:href="@{/login}" th:unless=${session.user != null}>Login</a> />
    th:id    　　   替换id    　　　       　     <input th:id="'xxx' + ${collect.id}"/>
    th:if          判断条件    　　　　          　<a th:if="${userId == collect.userId}" >
    th:include     布局标签，替换内容到引入的文件     <head th:include="layout :: htmlhead" th:with="title='xx'"></head> />
    th:fragment    布局标签，定义代码片段，其它引用   <div th:fragment="alert">
    th:object      替换对象    　　　            　 <div th:object="${session.user}">
    th:src         图片类地址引入    　　          　![](@{/img/logo.png})
    th:replace     布局标签，替换整个标签到引入的文件  <div th:replace="fragments/header :: title"></div>
    th:text    　  文本替换    　　               　<p th:text="${collect.description}">description</p>
    th:value       属性赋值    　　                <input th:value="${user.name}" />
    th:inline      定义js脚本可以使用变量           <script type="text/javascript" th:inline="javascript">
    th:remove      删除某个属性    　　　        　 <tr th:remove="all"> 
    th:style       设置样式    　　　　　　　　      th:style="'display:' + @{(${sitrue} ? 'none' : 'inline-block')} + ''"
    th:onclick     点击事件    　　　　　　         th:onclick="'getCollect()'"

    ```  
