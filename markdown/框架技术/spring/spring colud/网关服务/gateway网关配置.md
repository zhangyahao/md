1.  配置： 
     1.   请求参数配置       
             ```yaml
                server:
                  port: 8080
                spring:
                  application:
                  	 name: api-gateway
                  cloud:
                    gateway:
                      routes:
                        -id: gateway-service
                          uri: https://www.baidu.com
                          order: 0
                          predicates:
                            -Query=smile
    
             ```
             同时还可以对值进行匹配设置
             ```yaml
               server:
                 port: 8080
               
                 application:
               
                   name: api-gateway
                 cloud:
                   gateway:
                     routes:
                       -id: gateway-service
                         uri: https://www.baidu.com
                         order: 0
                         predicates:
                           -Query=keep, pu.
             ```
             只有当请求中包含 keep属性且参数以pu开头长度为三位可叠加使用  
             ```yaml
                 server:
                   port: 8080
                 spring:
                   cloud:
                     gateway:
                       routes:
                       - id: neo_route
                         uri: http://localhost:2001/
                         predicates:
                         - Path=/discoveryClient
                         - Query=smile
                         - Query=keep, pu.
    
             ```
     2.   Path 的匹配转发  
          ````yaml
            spring:
              cloud:
                gateway:
                  routes:
                    - id: path_route
            uri: http://c.biancheng.net
            predicates:
              - Path=/spring_cloud
          ````            