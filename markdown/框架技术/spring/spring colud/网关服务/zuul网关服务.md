网关： 是一个网络整体系统中的前置门户入口。请求首先通过网关，进行路径的路由，定位到具体的服务节点上。  

Zuul是一个微服务网关，首先是一个微服务。也是会在Eureka注册中心中进行服务的注册和发现。也是一个网关，请求应该通过Zuul来进行路由。  

使用Zuul，一般在微服务数量较多（多于10个）的时候推荐使用，对服务的管理有严格要求的时候推荐使用，当微服务权限要求严格的时候推荐使用。  

Zuul网关不是必要的。是推荐使用的。


1.  ###作用
    1.  统一入口：未全部为服务提供一个唯一的入口，网关起到外部和内部隔离的作用，保障了后台服务的安全性。
    2.  鉴权校验：识别每个请求的权限，拒绝不符合要求的请求。
    3.  动态路由：动态的将请求路由到不同的后端集群中。
    4.  减少客户端与服务端的耦合：服务可以独立发展，通过网关层来做映射。

2.  ###使用  
    1.  网关访问方式    
        通过zuul访问服务的，URL地址默认格式为：http://zuulHostIp:port/要访问的服务名称/服务中的URL
    2.  网关依赖  
        ````text
            <!-- spring cloud Eureka Client 启动器 -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-eureka</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-zuul</artifactId>
            </dependency>
            <!-- zuul网关的重试机制，不是使用ribbon内置的重试机制
               是借助spring-retry组件实现的重试
               开启zuul网关重试机制需要增加下述依赖
             -->
            <dependency>
               <groupId>org.springframework.retry</groupId>
               <artifactId>spring-retry</artifactId>
            </dependency>
        ````    
    3.  网关启动器  
        ```java
                /**
                 * @EnableZuulProxy - 开启Zuul网关。
                 *  当前应用是一个Zuul微服务网关。会在Eureka注册中心中注册当前服务。并发现其他的服务。
                 *  Zuul需要的必要依赖是spring-cloud-starter-zuul。
                 */
                @SpringBootApplication
                @EnableZuulProxy
                public class ZuulApplication {
                    public static void main(String[] args) {
                        SpringApplication.run(ZuulApplication.class, args);
                    }
                }

        ```
    4.  配置  
        1.  url路径配置
            ```properties
                # URL pattern
                # 使用路径方式匹配路由规则。
                # 参数key结构： zuul.routes.customName.path=xxx
                # 用于配置路径匹配规则。
                # 其中customName自定义。通常使用要调用的服务名称，方便后期管理
                # 可使用的通配符有： * ** ?
                # ? 单个字符
                # * 任意多个字符，不包含多级路径
                # ** 任意多个字符，包含多级路径
                zuul.routes.eureka-application-service.path=/api/**
                # 参数key结构： zuul.routes.customName.url=xxx
                # url用于配置符合path的请求路径路由到的服务地址。
                zuul.routes.eureka-application-service.url=http://127.0.0.1:8080/

            ```    
        2.  服务名称匹配 ，**常用方式**  
            ```properties
                # service id pattern 通过服务名称路由
                # key结构 ： zuul.routes.customName.path=xxx
                # 路径匹配规则
                zuul.routes.eureka-application-service.path=/api/**
                # key结构 ： zuul.routes.customName.serviceId=xxx
                # serviceId用于配置符合path的请求路径路由到的服务名称。  
                zuul.routes.eureka-application-service.serviceId=eureka-application-service

            ```
            **也可以简化** 
             ```properties  
                # simple service id pattern 简化配置方案
                # 如果只配置path，不配置serviceId。则customName相当于服务名称。
                # 符合path的请求路径直接路由到customName对应的服务上。
                zuul.routes.eureka-application-service.path=/api/**
             ```
        3.   路由排除配置  
             ```properties
                # ignored service id pattern
                # 配置不被zuul管理的服务列表。多个服务名称使用逗号','分隔。
                # 配置的服务将不被zuul代理。
                zuul.ignored-services=eureka-application-service
                # 此方式相当于给所有新发现的服务默认排除zuul网关访问方式，只有配置了路由网关的服务才可以通过zuul网关访问
                # 通配方式配置排除列表。
                zuul.ignored-services=*
                # 使用服务名称匹配规则配置路由列表，相当于只对已配置的服务提供网关代理。
                zuul.routes.eureka-application-service.path=/api/**
                
                # 通配方式配置排除网关代理路径。所有符合ignored-patterns的请求路径都不被zuul网关代理。
                zuul.ignored-patterns=/**/test/**
                zuul.routes.eureka-application-service.path=/api/**

             ```
        4.   路由前缀配置  
             ```properties
                # prefix URL pattern 前缀路由匹配
                # 配置请求路径前缀，所有基于此前缀的请求都由zuul网关提供代理。
                zuul.prefix=/api
                # 使用服务名称匹配方式配置请求路径规则。
                # 这里的配置将为：http://ip:port/api/appservice/**的请求提供zuul网关代理，可以将要访问服务进行前缀分类。
                # 并将请求路由到服务eureka-application-service中。
                zuul.routes.eureka-application-service.path=/appservice/**
             ```     
    5.  使用：  
          ```java
                package com.example.zuul.filter;
                
                import com.netflix.zuul.ZuulFilter;
                import com.netflix.zuul.context.RequestContext;
                import com.netflix.zuul.exception.ZuulException;
                import io.micrometer.core.instrument.util.StringUtils;
                import org.springframework.beans.factory.annotation.Value;
                import org.springframework.stereotype.Component;
                
                import javax.servlet.http.HttpServletRequest;
                
                @Component
                public class TokenFilter extends ZuulFilter {
                	//统计当前Zuul调用次数
                    int count = 0;
                    
                	//获取Zuul服务端口号
                    @Value("${server.port}")
                    private String prot;
                    
                    /**
                     * 指定该Filter的类型
                     * ERROR_TYPE = "error";
                     * POST_TYPE = "post";
                     * PRE_TYPE = "pre";
                     * ROUTE_TYPE = "route";
                     */
                    @Override
                    public String filterType() {
                        System.out.println("filterType()...");
                        return "pre";
                    }
                
                
                    /**
                     * 指定该Filter执行的顺序（Filter从小到大执行）
                     * DEBUG_FILTER_ORDER = 1;
                     * FORM_BODY_WRAPPER_FILTER_ORDER = -1;
                     * PRE_DECORATION_FILTER_ORDER = 5;
                     * RIBBON_ROUTING_FILTER_ORDER = 10;
                     * SEND_ERROR_FILTER_ORDER = 0;
                     * SEND_FORWARD_FILTER_ORDER = 500;
                     * SEND_RESPONSE_FILTER_ORDER = 1000;
                     * SIMPLE_HOST_ROUTING_FILTER_ORDER = 100;
                     * SERVLET_30_WRAPPER_FILTER_ORDER = -2;
                     * SERVLET_DETECTION_FILTER_ORDER = -3;
                     */
                    @Override
                    public int filterOrder() {
                        System.out.println("filterOrder()...");
                        return 0;
                    }
                
                    /**
                     * 指定需要执行该Filter的规则
                     * 返回true则执行run()
                     * 返回false则不执行run()
                     */
                    @Override
                    public boolean shouldFilter() {
                        System.out.println("shouldFilter()...");
                        return true;
                    }
                
                    /**
                     * 该Filter具体的执行活动
                     */
                    @Override
                    public Object run() throws ZuulException {
                        // 获取上下文
                        //RequestContext currentContext = RequestContext.getCurrentContext();
                        //HttpServletRequest request = currentContext.getRequest();
                        //获取userToken
                       // String userToken = request.getParameter("userToken");
                        //System.out.println("userToken: "+userToken);
                        //if (StringUtils.isEmpty(userToken)) {
                            //不会继续执行调用服务接口，网关直接响应给客户端
                            //currentContext.setSendZuulResponse(false);
                            //currentContext.setResponseStatusCode(401);
                           // currentContext.setResponseBody("userToken is Null");
                           // return null;
                       // }else if(!userToken.equals("10010")){
                           // currentContext.setSendZuulResponse(false);
                            //currentContext.setResponseStatusCode(401);
                            //currentContext.setResponseBody("userToken is Error");
                            //return null;
                        //}
                        // 否则正常执行业务逻辑，调用服务.....
                        System.out.println("访问Zuul网关端口为："+prot +"（total："+ ( count++) +"）");
                        return null;
                
                    }
                }

          ```       
    
    


