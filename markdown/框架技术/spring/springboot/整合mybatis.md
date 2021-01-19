1. 整合mybatis
   1. 生成的 mapper文件  和dao层中的需要添加对应关系注解
        ```aidl
          @Mapper
          @Component(value = "xxxxxx")

        ```
   2.  启动类需要添加注解
        ```aidl
           @MapperScan("xxx.dao")
           xxx.dao dao层包位置
        ```
    