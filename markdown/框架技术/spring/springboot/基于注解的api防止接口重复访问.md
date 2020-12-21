[原文地址](https://www.cnblogs.com/brxHqs/p/9771447.html)

1. 编写AccessLimit注解
    ```java
           import java.lang.annotation.ElementType;
           import java.lang.annotation.Retention;
           import java.lang.annotation.RetentionPolicy;
           import java.lang.annotation.Target;
           @Retention(RetentionPolicy.RUNTIME)
           @Target(ElementType.METHOD)
          public @interface AccessLimit {
              //控制单位时间内最大访问次数
              int maxCount();
          
              int seconds();
              //是否需要用户身份
              boolean needLogin() default true;
          
          }

   
    ```
2.   编写拦截器
        ```java
         @Component
         public class AccessInterceptor  extends HandlerInterceptorAdapter{
             
             @Autowired
             MiaoshaUserService userService;
             
             @Autowired
             RedisService redisService;
             
             @Override
             public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                     throws Exception {
                 if(handler instanceof HandlerMethod) {
                     MiaoshaUser user = getUser(request, response);
                     UserContext.setUser(user);// 请求（一个thread）->拦截器->AOP->目标方法->清理user
                     HandlerMethod hm = (HandlerMethod)handler;//对controller里的方法做了封装，避免运行时使用反射
                     AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
                     if(accessLimit == null) {
                         return true;
                     }
                     int seconds = accessLimit.seconds();
                     int maxCount = accessLimit.maxCount();
                     boolean needLogin = accessLimit.needLogin();
                     String key = request.getRequestURI();
                     if(needLogin) {
                         if(user == null) {
                             render(response, CodeMsg.SESSION_ERROR);//可以重定向到登陆或向浏览器输出json
                             return false;//返回false拦截，返回true放行
                         }
                         key += "_" + user.getId();//  接口防刷：需要用户登陆的 接口路径+用户id做key  不要的：接口路径做key
                     }else {
                         //do nothing
                     }
         　　　　　　　　//redis限速器
                     AccessKey ak = AccessKey.withExpire(seconds);
                     Integer count = redisService.get(ak, key, Integer.class);
                     if(count  == null) {
                          redisService.set(ak, key, 1);
                     }else if(count < maxCount) {
                          redisService.incr(ak, key);
                     }else {
                         render(response, CodeMsg.ACCESS_LIMIT_REACHED);//访问 达到 限制
                         return false;
                     }
                 }
                 return true;
             }
         
             //向浏览器写出json
             private void render(HttpServletResponse response, CodeMsg cm)throws Exception {
                 response.setContentType("application/json;charset=UTF-8");
                 OutputStream out = response.getOutputStream();
                 String str  = JSON.toJSONString(Result.error(cm));
                 out.write(str.getBytes("UTF-8"));// out.write(byte[])
                 out.flush();
                 out.close();
             }
         
             //从请求中获取用户信息
             private MiaoshaUser getUser(HttpServletRequest request, HttpServletResponse response) {
         　　//从request或cookie中获取token，防止不支持cookie的client
                 String paramToken = request.getParameter(MiaoshaUserService.COOKI_NAME_TOKEN);
                 String cookieToken = getCookieValue(request, MiaoshaUserService.COOKI_NAME_TOKEN);
                 if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
                     return null;
                 }
                 String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
                 return userService.getByToken(response, token);// service里也是返回null，最上级在根据null处理异常
             }
             
             private String getCookieValue(HttpServletRequest request, String cookiName) {
                 Cookie[]  cookies = request.getCookies();
                 if(cookies == null || cookies.length <= 0){
                     return null;
                 }
                 for(Cookie cookie : cookies) {
                     if(cookie.getName().equals(cookiName)) {
                         return cookie.getValue();
                     }
                 }
                 return null;
             }
        ```
3.  使用     
    ```java 
        @AccessLimit(seconds=5, maxCount=5, needLogin=true)
        @RequestMapping(value="/path", method=RequestMethod.GET)
        @ResponseBody
        public Result<String> getMiaoshaPath(HttpServletRequest request, MiaoshaUser user）{...}
    ```