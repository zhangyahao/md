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
       
    2.   @Primary:因为spring中bean都是单例 当一个类需要两个实例时 就需要采取措施 @Primary就是为了解决这个问题   
        同时此注解的实例优先于其他实例注解
        
4.  Transactional :事物注解方式<br>
    1.  注意事项：<br>
        1.  只能被应用到public方法上, 对于其它非public的方法,如果标记了@Transactional也不会报错,但方法没有事务功能
        2.   用 spring 事务管理器,由spring来负责数据库的打开,提交,回滚.默认遇到运行期例外(throw new RuntimeException("注释");)<br>
             会回滚，即遇到不受检查（unchecked）的例外时回滚；而遇到需要捕获的例外(throw new Exception("注释");)不会回滚,即遇到<br>
             受检查的例外（就是非运行时抛出的异常，编译器会检查到的异常叫受检查例外或说受检查异常）时，需我们指定方式来让事务回滚<br>
             要想所有异常都回滚,要加上 @Transactional( rollbackFor={Exception.class,其它异常}) .如果让unchecked例外不回滚：<br>
             ```aidl
                @Transactional(rollbackFor=Exception.class) //指定回滚,遇到异常Exception时回滚
                                 public void methodName() {
                                 　　　throw new Exception("注释");
                                 }
                                 @Transactional(noRollbackFor=Exception.class)//指定不回滚,遇到运行期例外(throw new RuntimeException("注释");)会回滚
                                 public ItimDaoImpl getItemDaoImpl() {
                                 　　　throw new RuntimeException("注释");
                                 } 
            ```
        3.  Spring团队的建议是你在具体的类（或类的方法）上使用 @Transactional 注解，而不要使用在类所要实现的任何接口上。因为注解是<br>
            不能被继承得
            
    2.  注解中常用参数说明<br>    
        
        | 参数名称 | 功能描述|
        |:------:|-----|
        |readOnly|该属性用于设置当前事务是否为只读事务，设置为true表示只读，false则表示可读写，默认值为false。例如：@Transactional(readOnly=true)|
        |rollbackFor|该属性用于设置需要进行回滚的异常类数组，当方法中抛出指定异常数组中的异常时，则进行事务回滚。例如：|
        |           |指定单一异常类：@Transactional(rollbackFor=RuntimeException.class)|
        |           |指定多个异常类：@Transactional(rollbackFor={RuntimeException.class, Exception.class})|
        |rollbackForClassName|该属性用于设置需要进行回滚的异常类名称数组，当方法中抛出指定异常名称数组中的异常时，则进行事务回滚。例如:|
        |           |指定单一异常类名称：@Transactional(rollbackForClassName="RuntimeException")|
        |           |指定多个异常类名称：@Transactional(rollbackForClassName={"RuntimeException","Exception"})|
        |noRollbackFor|该属性用于设置不需要进行回滚的异常类数组，当方法中抛出指定异常数组中的异常时，不进行事务回滚。例如：|
        |           |指定单一异常类：@Transactional(noRollbackFor=RuntimeException.class)|
        |            |指定多个异常类：@Transactional(noRollbackFor={RuntimeException.class, Exception.class})|
        |noRollbackForClassName|该属性用于设置不需要进行回滚的异常类名称数组，当方法中抛出指定异常名称数组中的异常时，不进行事务回滚。例如：|
        |           |指定单一异常类名称：@Transactional(noRollbackForClassName="RuntimeException")|
        |           |指定多个异常类名称:@Transactional(noRollbackForClassName={"RuntimeException","Exception"})|
        |propagation|该属性用于设置事务的传播行为|
        |isolation|属性用于设置底层数据库的事务隔离级别，事务隔离级别用于处理多事务并发的情况，通常使用数据库的默认隔离级别即可，基本不需要进行设置|
        |timeout|该属性用于设置事务的超时秒数，默认值为-1表示永不超时|
        
    3. 事务隔离级别:<br>
        1.  @Transactional(isolation = Isolation.READ_UNCOMMITTED)：读取未提交数据(会出现脏读, 不可重复读) 基本不使用
        2.  @Transactional(isolation = Isolation.READ_COMMITTED)：读取已提交数据(会出现不可重复读和幻读)
        3.  @Transactional(isolation = Isolation.REPEATABLE_READ)：可重复读(会出现幻读)
        4.  @Transactional(isolation = Isolation.SERIALIZABLE)：串行化
        5.MYSQL: 默认为REPEATABLE_READ级别，SQLSERVER: 默认为READ_COMMITTED
        
          　　
            