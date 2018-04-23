#spring注解
1. 声明bean注解
    1. @Component 泛指组件，当组件不好归类的时候，我们可以使用这个注解进行标注。
    2. @service   用于service层
    3. @Controller 用于标注控制层组件（如struts中的action）
    4. @Repository  用于dao层
2. 注入Bean注解
    1. @Autowired  直接注入
    2. @Resource  默认按名称装配，当找不到与名称匹配的bean才会按类型装配。
3.  配置bean
    1. @Configuration：在方法上直接将bean交给spring 放弃使用配置文件    
      ```aidl
      <beans> 
              <bean id="orderService" class="com.acme.OrderService"/> 
                      <constructor-arg ref="orderRepository"/> 
              </bean> 
              <bean id="orderRepository" class="com.acme.OrderRepository"/> 
                      <constructor-arg ref="dataSource"/> 
              </bean> 
      </beans> 

       ```
       可以使用注解变为
       ```aidl
       @Configuration 
       public class ApplicationConfig { 
          
               public @Bean OrderService orderService() { 
                       return new OrderService(orderRepository()); 
               } 
          
               public @Bean OrderRepository orderRepository() { 
                       return new OrderRepository(dataSource()); 
               } 
          
               public @Bean DataSource dataSource() { 
                       // instantiate and return an new DataSource … 
               } 
       } 
   

       ```