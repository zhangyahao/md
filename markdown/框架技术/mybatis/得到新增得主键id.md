1.  自增主键
      ```xml
         <!--新增信息，并拿到新增信息的表主键信息。
               新增数据，得到主键的外层写法没什么特别，跟普通的insert一样。只不过里面加了selectKey-->
           <insert id="insertAndgetkey" parameterType="com.soft.mybatis.model.User">
               <!--selectKey  会将 SELECT LAST_INSERT_ID()的结果放入到传入的model的主键里面，
                   keyProperty 对应的model中的主键的属性名，这里是 user 中的id，因为它跟数据库的主键对应
                   order AFTER 表示 SELECT LAST_INSERT_ID() 在insert执行之后执行,多用与自增主键，
                         BEFORE 表示 SELECT LAST_INSERT_ID() 在insert执行之前执行，这样的话就拿不到主键了，
                               这种适合那种主键不是自增的类型
                   resultType 主键类型 -->
               <selectKey keyProperty="id" order="AFTER" resultType="java.lang.Integer">
                   SELECT LAST_INSERT_ID()
               </selectKey>
               insert into t_user (username,password,create_date) values(#{username},#{password},#{createDate})
            </insert>
      ```
2.  无序得uid
    ```xml
           <!-- 跟普通的insert没有什么不同的地方 -->
            <insert id="insert" parameterType="com.soft.mybatis.model.Customer">
                <!-- 跟自增主键方式相比，这里的不同之处只有两点
                            1  insert语句需要写id字段了，并且 values里面也不能省略
                            2 selectKey 的order属性需要写成BEFORE 因为这样才能将生成的uuid主键放入到model中，
                            这样后面的insert的values里面的id才不会获取为空
                      跟自增主键相比就这点区别，当然了这里的获取主键id的方式为 select uuid()
                      当然也可以另写别生成函数。-->
                <selectKey keyProperty="id" order="BEFORE" resultType="String">
                    select uuid()
                </selectKey>
                insert into t_customer (id,c_name,c_sex,c_ceroNo,c_ceroType,c_age)
                values (#{id},#{name},#{sex},#{ceroNo},#{ceroType},#{age})
            </insert>

    ```