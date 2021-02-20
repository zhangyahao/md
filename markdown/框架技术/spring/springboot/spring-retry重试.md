1.  在项目中,调用第三方接口响应比较慢,或者由于网络抖动等原因,导致无响应的情况,就要用到重试机制.比较简单成熟的方案就是使用spring-retry功能,  
    spring-retry需要使用aop的特性,所以引入aspectj
2.  项目依赖  
    ```text
        <dependency>
            <groupId>org.springframework.retry</groupId>
            <artifactId>spring-retry</artifactId>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
        </dependency>
    ```    
3.  核心注解  
    `@EnableRetry` `@Retryable`  `@Recover`    
    
    1.  `@EnableRetry`:此注解用于开启重试框架，可以修饰在SpringBoot启动类上面，也可以修饰在需要重试的类上  
    2.  `@Retryable`:     
              1.   value：Class[]类型，用于指定需要重试的异常类型，   
              2.   include：Class[]类型，作用于value类似，区别尚未分析
              3.   exclude：Class[]类型，指定不需要重试的异常类型
              4.   maxAttemps：int类型，指定最多重试次数，默认3
              5.   backoff：Backoff类型，指明补偿机制  
                      delay/value:指定延迟后重试，默认为1000L,即1s后开始重试 ;
                      multiplier:指定延迟的倍数
                      maxDelay:最大延迟毫秒数
    3.  `@Recover`:   
            当重试次数耗尽依然出现异常时，执行此异常对应的@Recover方法。
             异常类型需要与Recover方法参数类型保持一致，
             recover方法返回值需要与重试方法返回值保证一致 
4.  使用：  
       1.  开启重试：   
            ```java
               @SpringBootApplication
               @EnableRetry
               public class ReadscoreApplication {
               
                   public static void main(String[] args) {
                       SpringApplication.run(ReadscoreApplication.class, args);
                   }
               
               }
            ```           
       2.   对方法开启重试：  
            ```text
                @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 2000, multiplier = 1.5))
                public String download() throws Exception {
                    // 模拟测试
                    if(StringUtils.isBlank(null)){
                        log.info("重试查询下载链接: {}", LocalTime.now());
                        throw new Exception("正在导出文件,请稍后");
                    }else {
                        return result;
                    }
                }
           ```    
       3.  定义回调,注意异常类型和方法返回值类型要与重试方法一致  
            ```text
               @Recover
               public String recover(Exception e){
                   log.error("获取下载链接失败! {}", LocalTime.now());
                   throw new Exception("获取下载链接失败!");
               }

            ```    
5.  注意：  
     1.  对重试方法不能使用回调              
     2.  recover回调报错
         ```text
            org.springframework.retry.ExhaustedRetryException: Cannot locate recovery method
            报错显示找不到recovery方法
         ```
         解决：
         1.  异常类型需要与Recover方法参数类型保持一致
         2.  recover方法返回值需要与重试方法返回值保证一致