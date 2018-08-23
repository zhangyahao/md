[SpringBoot：使用小技巧合集](https://mp.weixin.qq.com/s?__biz=MzAxNDMwMTMwMw==&mid=2247489249&idx=1&sn=461be4d5729079f590af81100f7f7f4a&chksm=9b9437f9ace3beef9c1fcc83e18de5a0905ec5786511c878865305aff11008895e1c51389396&mpshare=1&scene=1&srcid=0823Iz4H6SkX4ceeoCpDO3L7#rd)

###新建项目 

spring官网提供了方法 <br>
[官网提供的快速方法](https://start.spring.io/)

###更改服务器的启动端口号

在配置文件中 加 **server.port=端口号**

###加项目访问时的项目名
同上 **server.context-path=/项目名**

###更改项目启动时的logo

在resource中新建一个banner.txt 文件 将logo 直接放入

或者在配置文件中配置 **banner.location=classpath:位置**（classpath默认在resource中）

然后放入一个配置的文件

###设置网站图标
原来我们在使用 tomcat开发时，设置网站图片时，即icon图标时，一般都是直接替换 root包下的 favicon.ico<br>
替换成自己的，或者在网页的头部设置 link的ref为 icon然后设置其 href值。而在 SpringBoot中，替换图片也是<br>
很简单的，只需要将自定义图片放置在 静态资源目录下即可，即默认有 static、 public、 resources、 /META-INF/resources<br>
或者自定义的静态目录下即可。<br>


###允许跨域访问
````aidl
CORS是一个W3C标准，全称是"跨域资源共享"（Cross-origin resource sharing）。它允许浏览器向跨源(协议 + 域名 + 端口)服务器，<br>
发出XMLHttpRequest请求，从而克服了AJAX只能同源使用的限制。
````
0. 利用 @CrossOrigin注解，可放至在类上或者方法上。类上代表整个控制层所有的映射方法都支持跨域请求。
    ```aidl
    @CrossOrigin(origins= "http://blog.lqdev.cn",maxAge =3600)
    @RestController
    public class  demoController{
    @GetMapper("/")
    public  String index (){
           return "hello,CORS";
        }
     }
    ```
1.  配置全局 CORS配置。官网也有给出实例，具体如下:
    ```aidl
    @Configuration
    public class MyConfiguration{
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurerAdapter(){
        @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/api/**").allowedOrigins("http://blog.lqdev.cn);
                }
            }
        }
    }

    ```
    
    
###独立Tomcat运行
0. 修改pom打包方式为 war，同时排除了内置的 tomcat。
     ```aidl
      <!-- 排除内置的tomcat -->
           <packaging>war</packaging>
             <dependency>
                 <groupId>org.springframework.boot</groupId>
                 <artifactId>  spring-boot-starter-tomcat</artifactId>
                 <scope> compile</scope>
             </dependency>
             <!-- 若直接有使用servlet对象时，需要将servlet引入，本例是没有的~ -->
             <dependency>
                 <groupId>javax.servlet</groupId>
                 <artifactId> javax.servlet-api</artifactId>
                 <scope> provided</scope>
             </dependency>

    ```
###启动不设置端口
1.  修改配置文件的属性：
    ```aidl
    spring.main.web-environment=false
    ```
   