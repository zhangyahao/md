### spring 的安全框架  全java配置
1.  作用
    1.  权限认证 类似shiro 
    
2.  使用
    1. 使角色方法不包含ROLE_  **_4.0的新功能_**
        `<intercept-url pattern="/**" access="hasRole('ROLE_USER')"/>`
        包含
        `<intercept-url pattern="/**" access="hasRole('USER')"/>`
        或者直接使用注解`@PreAuthorize("hasRole('ROLE_USER')")`
        
    2.   **_3.2开始支持注解_**
    3.   创建过滤器，负责所有的安全操作，例如url，验证用户名和密码等等.  
         ```java
             import org.springframework.beans.factory.annotation.Autowired;
                            
             import org.springframework.context.annotation.*;
             import org.springframework.security.config.annotation.authentication.builders.*;
             import org.springframework.security.config.annotation.web.configuration.*;
             
             @EnableWebSecurity
             public class SecurityConfig extends WebSecurityConfigurerAdapter {
             
                 @Autowired
                 public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                     auth.inMemoryAuthentication()
                         .withUser("user").password("password").roles("USER");// 配置用户
                 }
                            }
                            

         ```
         方法名不重要，重要的是只能在一个被@EnableWebSecurity，@EnableGlobalMethodSecurity或者
          @EnableGlobalAuthentication 注解的类中配置AuthenticationManagerBuilder。
                
           在其中虽然没有写太多的配置，但其内部实现了很多的功能。比如  
                 
                1.  在你的应用程序中对每个URL进行验证
                2.  为你生成一个登陆表单
                3.  允许用户使用用户名user和密码password使用验证表单进行验证。
                4.  允许用户登出
                5.  CSRF 攻击防范
                6.  Session保护
                7.  安全 Header 集成
    4.  将上面的过滤器进行配置使用
           1.  如果使用的是spring，使用spring后，在spring中就会有一个`WebApplicationInitializer`载入spring的配置，我们应当
                将AbstractSecurityWebApplicationInitializer 交给已存在的`WebApplicationInitializer`来管理
                `
                   import org.springframework.security.web.context.*;
                 
                    public class SecurityWebApplicationInitializer
                     extends AbstractSecurityWebApplicationInitializer {
                 
                 }
                 `
                 
           2.  对于springmvc来说，因为在创建时可以有多个`DispatcherServlet`，因为要将
                `WebApplicationInitializer`加入到`getRootConfigClasses`中
                `
                    public class MvcWebApplicationInitializer extends
                            AbstractAnnotationConfigDispatcherServletInitializer {
                    
                        @Override
                        protected Class<?>[] getRootConfigClasses() {
                            return new Class[] { SecurityConfig.class };
                        }
                    
                        // ... other overrides ...
                    }
                `
                
    5.    http安全验证<br>
           提供基于表单的验证 在`WebApplicationInitializer` 中添加默认方法<br>
           在jsp或者html表单中 登录名参数必须被命名为username密码参数必须被命名为password<br>
          ```aidl
          
              protected void configure(HttpSecurity http) throws Exception {
                  http
                      .authorizeRequests()
                          .anyRequest().authenticated()// 所有的请求都需要用户被认证
                          .and()
                      .formLogin()// 允许表单提交
                      .loginPage("/login") //登录页面的位置
                      .permitAll();  // 允许所有的用户登录
                          .and()
                      .httpBasic(); //允许http基本验证进行验证
              }

          ```

    6. **_权限控制_**                 
           ```
              protected void configure(HttpSecurity http) throws Exception {
                  http
                      .authorizeRequests() 
                          .antMatchers("/resources/**", "/signup", "/about").permitAll() //所有用户都可以登录
                          .antMatchers("/admin/**").hasRole("ADMIN") //admin才可以登录
                          .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")            4
                          .anyRequest().authenticated() //所有的url要求用户验证
                          .and()
                      // ...
                      .formLogin();
              }
           ```
    7.  登出处理   
        登出后执行以下动作              
            1.  是session无效
            2.  清楚所有已配置的RememberMe配置
            3.  清除SecurityContextHolder，SecurityContextHolder是SpringSecurity最基本的组件了，
            是用来存放SecurityContext的对象
            4.  跳转到/login?success
            
          ```aidl
          protected void configure(HttpSecurity http) throws Exception {
              http
                  .logout()  // 登出
                      .logoutUrl("/my/logout")// 登出页面
                      .logoutSuccessUrl("/my/index") // 登出成功后跳转页面
                      .logoutSuccessHandler(logoutSuccessHandler)//让你设置定制的LogoutSuccessHandler,如果指定
                                                                 //了这个选项那么logoutSuccessUrl()的设置会被忽略
                      .invalidateHttpSession(true)  // 注销后是否使HttpSession无效
                      .addLogoutHandler(logoutHandler) // 添加一个logoutHandler,默认SecurityContextLogoutHandler会被添
                                                       //加为最后一个logoutHandler.
                      .deleteCookies(cookieNamesToClear) //注销成功后移除coolie
                      .and()
                  ...
          }
          ```
    8. 配置多个用户到内存中，并赋予不同的权限
        ```aidl
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth
                .inMemoryAuthentication()
                    .withUser("user").password("password").roles("USER").and()
                    .withUser("admin").password("password").roles("USER", "ADMIN");
        }

        ```      
    9. 对数据库的支持
        ```aidl
           @Autowired
           private DataSource dataSource;
           
           @Autowired
           public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
               auth
                   .jdbcAuthentication()
                       .dataSource(dataSource)
                       .withDefaultSchema()
                       .withUser("user").password("password").roles("USER").and()
                       .withUser("admin").password("password").roles("USER", "ADMIN");
           }

        ```
    10. 对不同的url 进行配置
        ```aidl
        @EnableWebSecurity
        public class MultiHttpSecurityConfig {
            @Autowired
            public void configureGlobal(AuthenticationManagerBuilder auth) { //正常的配置
                auth
                    .inMemoryAuthentication()
                        .withUser("user").password("password").roles("USER").and()
                        .withUser("admin").password("password").roles("USER", "ADMIN");
            }
        
                @Configuration
             @Order(1)  // 优先度
                public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
                protected void configure(HttpSecurity http) throws Exception {
                    http
                        .antMatcher("/api/**")  // 3
                        .authorizeRequests()
                            .anyRequest().hasRole("ADMIN")
                            .and()
                        .httpBasic();
                }
            }
        
            @Configuration // 没有优先度的情况下默认最后
            public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        
                @Override
                protected void configure(HttpSecurity http) throws Exception {
                    http
                        .authorizeRequests()
                            .anyRequest().authenticated()
                            .and()
                        .formLogin();
                }
             }
         }

         ```
   
    
            

