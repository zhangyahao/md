1.  `#`使用预编译，`$`使用拼接SQL。


####Mybatis框架下易产生SQL注入漏洞的情况主要分为以下：
1.  模糊查询  
    ```sql
    Select * from news where title like ‘%#{title}%’
    ```
    在这种情况下使用#程序会报错。应改为  
    ```sql
    select * from news where tile like concat(‘%’,#{title}, ‘%’)
    ```
2.  in 之后的多个参数  
    in之后多个id查询时使用# 同样会报错，    
    ```sql
        Select * from news where id in (#{ids})
     ```
    正确用法为使用foreach，而不是将#替换为$  
    ```mybatisognl
       Select * from news where id in
       <foreach collection="ids" item="item" open="("separatosr="," close=")">
       #{ids} 
       </foreach>
    ```