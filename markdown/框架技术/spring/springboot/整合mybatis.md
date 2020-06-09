1. 一些比较简单的sql可以不使用xml来完成<br>
   使用mybatis自带的注解就可以实现，注解有 
   `@Select
    @Insert
    @Update
    @Delete`
    
    简单的例子有:
    ```aidl
       @Mapper
       public interface UserMapper {
          @Select("select * from t_user")
          List<User> list();
       }

    ```
2.  要使用此注解需要在配置文件中配置：`mybatis.configuration.map-underscore-to-camel-case=true`    
3.  其他的一些比较麻烦的使用方式，个人感觉比较麻烦<br>
      [原文地址](https://mp.weixin.qq.com/s?__biz=MzAxNDMwMTMwMw==&mid=2247490260&idx=1&sn=bf9bda99c51d18c96a0171480d564ba2&chksm=9b943bccace3b2daed00fd61ff362b19735fcaa85e16655c4069f5dd1fe06eb65159ad7fd94c&mpshare=1&scene=1&srcid=1228uRxaDj9xFSnSIKTeMrLO#rd)
     