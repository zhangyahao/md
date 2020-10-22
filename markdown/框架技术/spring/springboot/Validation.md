1.  空和非空检查
     1.  `@NotBlank`：只能用于字符串不为 null ，并且字符串 `#trim()` 以后 length 要大于 0 。
     2.  `@NotEmpty`：集合对象的元素不为 0 ，即集合不为空，也可以用于字符串不为 null 。
     3.  `@NotNull`：不能为 null 。
     4.  `@Null`：必须为null。  
   
2.   数值检查
     1.   `@DecimalMax(value)`：被注释的元素必须是一个数字，其值必须小于等于指定的最大值。
     2.   `@DecimalMin(value)`：被注释的元素必须是一个数字，其值必须大于等于指定的最小值。
     3.   `@Digits(integer, fraction)`：被注释的元素必须是一个数字，其值必须在可接受的范围内。
     4.   `@Positive`：判断正数。
     5.   `@PositiveOrZero`：判断正数或 0 。
     6.   `@Max(value) `：该字段的值只能小于或等于该值。
     7.   `@Min(value)`：该字段的值只能大于或等于该值。
     8.   `@Negative`：判断负数。
     9.   `@NegativeOrZero`：判断负数或 0 。
3.   布尔值计算
     1.   `@AssertFalse`：必须为false。
     2.   `@AssertTrue`：必须为true。
4.   长度检查        
     1.    `@Size(max, min)`：检查该字段的 size 是否在 min 和 max 之间，可以是字符串、数组、集合、Map 等。
5.   日期检查
      1.   `@Future`：必须为未来时间
      2.   `@FutureOrPresent`：判断日期是否是将来或现在日期。
      3.   `@Past`：必须为过去时间
      4.   `@PastOrPresent`：判断日期是否是过去或现在日期。
6.    其它检查      
      1.   `@Email`：被注释的元素必须是电子邮箱地址。
      2.   `@Pattern(value)`：被注释的元素必须符合指定的正则表达式。
7.  需添加一个特定得异常全局处理来处理参数校验  
     ````java
            @RestControllerAdvice
            @ResponseBody
            public class CommonExceptionHandler {
            
                @ExceptionHandler({MethodArgumentNotValidException.class})
                @ResponseStatus(HttpStatus.OK)
            
                public ResponseEntity<JsonMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
                    BindingResult bindingResult = ex.getBindingResult();
                    StringBuilder sb = new StringBuilder("校验失败:");
                    for (FieldError fieldError : bindingResult.getFieldErrors()) {
                        sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
                    }
                    String msg = sb.toString();
                    return ResultInfo.dealWithResult(msg);
                }
            
                @ExceptionHandler({ConstraintViolationException.class})
                @ResponseStatus(HttpStatus.OK)
                public ResponseEntity<JsonMessage> handleConstraintViolationException(ConstraintViolationException ex) {
                    return ResultInfo.dealWithResult(HTTP_STATE_CODE_210);
                }
            }
     ````
    同时，还需添加pom  
     ```aidl
             <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-aop</artifactId>
              </dependency>
     ```
    以及在启动类上添加注解
    ```aidl
        @EnableAspectJAutoProxy(exposeProxy = true)
    ```
    