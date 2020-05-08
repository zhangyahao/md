1.  valida与Validated区别
     1.   声明式校验  
       Spring Validation 仅对 @Validated 注解，实现声明式校验。
     2.   分组校验  
       Bean Validation 提供的 @Valid 注解，因为没有分组校验的属性，所以无法提供分组校验。只能使用 ``@Validated` 注解。
     3.   嵌套校验  
        @Valid 注解的地方，多了【成员变量】。这就导致，如果有嵌套对象的时候，只能使用 @Valid 注解。例如说：    
       ```java
             // User.java
             public class User {
                 
                 private String id;
             
                 @Valid
                 private UserProfile profile;
             
             }
             
             // UserProfile.java
             public class UserProfile {
             
                 @NotBlank
                 private String nickname;
             
             }

       ```  
      如果不在 User.profile 属性上，添加 @Valid 注解，就会导致 UserProfile.nickname 属性，不会进行校验。
2.   总结        
    总的来说，绝大多数场景下，使用 @Validated 注解即可。
    
    而在有嵌套校验的场景，使用 @Valid 注解添加到成员属性上。
3.   使用    
   1.   需在spring boot启动类上添加注解  `@EnableAspectJAutoProxy(exposeProxy = true)`该注解将Spring AOP 能将当前代理对象设置到 AopContext 中
   2.   当实体类含有多个参数校验，值校验某个参数时，在该方法所在类上使用注解`@Validated`即可，若要使用该实体类，那么属于嵌套校验，在方法中  
        形参前添加注解 `@Valid`。例如 
        ```java
        @Service
        @Validated
        public class UserService {
        
            private Logger logger = LoggerFactory.getLogger(getClass());
        
            public void get(@Min(value = 1L, message = "编号必须大于 0") Integer id) {
                logger.info("[get][id: {}]", id);
            }
        
            public void add(@Valid UserAddDTO addDTO) {
                logger.info("[add][addDTO: {}]", addDTO);
            }
        
            public void add01(UserAddDTO addDTO) {
                this.add(addDTO);
            }
        
            public void add02(UserAddDTO addDTO) {
                self().add(addDTO);
            }
        
            private UserService self() {
                return (UserService) AopContext.currentProxy();
            }
        
        }
        ```
    
   
    