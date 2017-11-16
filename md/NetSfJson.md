1. 直接解析json
  ```$xslt
   JsonConfig config = new JsonConfig();  
    User user = new User(12L, "JSON", "json");
         System.out.println(JSONObject.fromObject(user, config).toString());
 ```
 2. 忽略某些属性
 ```$xslt
 JsonConfig config = new JsonConfig();  
 //忽略密码  
 config.setExcludes( new String[]{"password"});
 User user = new User(12L, "JSON", "json");
 System.out.println(JSONObject.fromObject(user, config).toString());
 ```
