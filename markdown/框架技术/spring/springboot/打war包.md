1.  pom文件中添加
    ````aidl
            
        <packaging>
            war
        </packaging>
        <!--  将tomcat移除，并加入相关倚赖-->
         <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                    <scope>provided</scope>
         </dependency>
    ````
2.  将启动类重新注册    
     ````aidl
        package com.cmcc.xxx.base;
        
        import com.cmcc.xxx.Application;
        import org.springframework.boot.builder.SpringApplicationBuilder;
        import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
        
        /**
         * @description:  springboot  tomcat启动
         * @author: Zhang
         **/
        public class ServletInitializer extends SpringBootServletInitializer {
            @Override
            protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
                return application.sources(Application.class);
            }
        }

    ````