1. 模糊搜索key
   ```$xslt
    Set<String> strings = jedis.keys("vod_*");
    Set<String> strings = jedis.keys("vod_?");
    ```
    1. `?`1个字符
    2. `*`0到任意多个字符
2.  清空数据    
    ```$xslt
    jedis.flushDB()
    ```
3. 判断某个键是否存在    
    ```$xslt
        jedis.exists("key")
    ```
4.  删除键    
    ```$xslt
    jedis.del("key")
    ```
5.  设置键key的过期时间,单位为秒   
    ```$xslt
    jedis.expire("key", 5)
    ```
6.  查看键key的剩余生存时间    
    ```$xslt
    jedis.ttl("key")
    ```
7. 移除键key的生存时间    
    ```$xslt
    jedis.persist("key")
    ```
8.  查看键username的剩余生存时间    
    ```$xslt
        jedis.ttl("username")
    ```
9.  查看键username所存储的值的类型    
    ```$xslt
    jedis.type("username")
    ```
10.  增加多个键值对    
    ```$xslt
        jedis.mset("key01","value01","key02","value02","key03","value03")
    ```
11.  获取多个键值对    
     ```$xslt
        jedis.mget("key01","key02","key03")
        ```
12.  删除多个键值对    
     ```$xslt
        jedis.del(new String[]{"key01","key02"})
     ```
13.  新增键值对防止覆盖原先值     
     ```$xslt
      jedis.setnx("key1", "value1")
     ```
14.   新增键值对并设置有效时间    
        ```$xslt
            jedis.setex("key3", 2, "value3")
            2为有效时间，单位为s
        ```
15.  获取原值，更新为新值    
      ```$xslt
      jedis.getSet("key2", "key2GetSet")
     ```
16.  获得key的值的字串
      ```$xslt
        jedis.getrange("key", 2, 4)
        获取value，并获取substring(2,4)
      ```     
17.  对整数和浮点数操作
        1.  key1的值加1：
            ```$xslt
                jedis.incr("key1")
            ```    
        2.  key2的值减1：
            ```$xslt
                jedis.decr("key2")
            ```
        3.  将key1的值加上整数5    
             ```$xslt
                jedis.incrBy("key1", 5)
             ```
        4.  将key2的值减去整数5:     
            ```$xslt
                jedis.decrBy("key2", 5)
            ```
18.  对list操作
        1.  存入
            ```aidl
                jedis.lpush(key,list)
                每次都会添加，不是覆盖
            ```            
        2.  取value中得一部分    
            ```aidl
                jedis.lrange("collections", 0, -1)//-1代表倒数第一个元素，-2代表倒数第二个元素
                jedis.lrange("collections",0,3)//collections区间0-3的元素
            ```
        3.  删除指定元素个数    
            ```aidl
                jedis.lrem("collections", 2, "HashMap")
             第二个参数为删除的个数（有重复时），后add进去的值先被删，类似于出栈
            ```
        4.  删除list0-3区间之外的元素    
            ```aidl
                jedis.ltrim("collections", 0, 3)
            ```
        5.  修改collections指定下标1的内容    
            ```aidl
            jedis.lset("collections", 1, "LinkedArrayList")
            ```
        6.  list排序    
            ```aidl
                jedis.sort("key")
            ```
        7.  获取list得长度        
            ```aidl
                jedis.llen("key")
            ```
        8.  获取下标为2的元素
            ```aidl
                jedis.lindex("key", 2)
            ```
19.  集合（Set）            
        1.  向集合中添加元素
            ```aidl
                jedis.sadd(key,value)
            ```
        2.  获取所有元素    
             ```aidl
                jedis.smembers("key")
             ```
        3.  删除一个指定元素     
            ```aidl
                jedis.srem("key", "value")
            ```
        4.  删除两个元素    
            ```aidl
                 jedis.srem("key", "value1","value2")
              ```
        5.  随机的移除集合中的一个元素      
            ```aidl
            jedis.spop("key")
            ```
        6.  包含元素的个数    
            ```aidl
                jedis.scard("key")
            ```
        7.  某个值是否存在key中    
            ```aidl
                jedis.sismember("key", "e3")
            ```
        8.  将eleSet1中删除e1并存入eleSet3中    
            ```aidl
                jedis.smove("eleSet1", "eleSet3", "e1")
            ```
        9.  eleSet1和eleSet2的交集    
            ```aidl
                jedis.sinter("eleSet1","eleSet2")
            ```
        10.  eleSet1和eleSet2的并集    
                ```aidl
                 jedis.sunion("eleSet1","eleSet2")
                ```
        11.  eleSet1和eleSet2的差集        
                ```aidl
                 jedis.sdiff("eleSet1","eleSet2")//eleSet1中有，eleSet2中没有
                ```
20.  map                
        1.  存入map
            ```aidl
                 jedis.hmset("hash",map);
            ```
        2.  map中添加一个键值对    
            ```aidl
                jedis.hset("hash", "key5", "value5")
            ```
        3. 散列key的所有键值对为    
            ````aidl
               jedis.hgetAll("key")
            ````
        4.  散列key的所有键为    
            ```aidl
                jedis.hkeys("key")
            ```
        5.  散列hash的所有值为     
            ```aidl
                jedis.hvals("key")
            ```
        6.  将key6保存的值加上一个整数，如果key6不存在则添加key6   
            ```aidl
                jedis.hincrBy("hash", "key6", 6)
                如果存在则将key6的值加上6，若不存在则增加6
            ```
        7.  删除一个或者多个键值对    
            ```aidl
                jedis.hdel("key", "mapKye1")
            ```
        8.  散列key中键值对的个数    
            ```aidl
                jedis.hlen("hash")
            ```
        9.  判断hash中是否存在key2    
            ```aidl
                jedis.hexists("hash","key2")
            ```
        10.  获取hash中的值    
               ```aidl  
                jedis.hmget("hash","key3","key4")
                ```
21.    其他的一些操作<br>
        [原文地址](https://mp.weixin.qq.com/s?__biz=MzUzMTA2NTU2Ng==&mid=2247485494&idx=1&sn=8e9337d90a71b9c9d5f7cc458af30554&chksm=fa497787cd3efe91ab6166f083a84fee606454f6b2229e062d5ae649b6249e45c39cd4c48bf0&scene=0#rd)                
                