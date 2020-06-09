1.  在controller层上配置 `RestController`  这样，默认的方法都会返回json
    ```aidl
        @RestController
        public class HelloWorldController {
            @RequestMapping("/getUser")
            public User getUser() {
            	User user=new User();
            	user.setUserName("小明");
            	user.setPassWord("xxxx");
                return user;
            }
    ```