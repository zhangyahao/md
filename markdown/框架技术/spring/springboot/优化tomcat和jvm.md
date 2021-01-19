1.  tomcat优化：
    ```yaml
        server:
          tomcat:
            min-spare-threads: 20
            max-threads: 100
          connection-timeout: 5000
    ```
2. jvm优化：   
    [优化jvm以及远程调试](https://mp.weixin.qq.com/s?__biz=MzI4Njc5NjM1NQ==&mid=2247488619&idx=1&sn=0b2410e495131500b7313e1ad1391c65&chksm=ebd62b47dca1a2518282f49e1a2f6b02ee6ae555aa9b0592b71ddbcb5885800d69238387dbc5&mpshare=1&scene=1&srcid=&key=01978776c216e5fadfe750f066e9f269a15cd0c14e81564d92cfb6fa13540d54e61f7f4b21afd23893bfb8b7356be60c67ba55527a4a68424350ed4636c58e06351e29b85fd89e1076b4ca972b1063ec&ascene=1&uin=MjUwNzEwMTIyMg%3D%3D&devicetype=Windows+10&version=62060739&lang=zh_CN&pass_ticket=Yt%2BFTtkOKrClA5V1v82pbHhVH5MSZh%2FLYeOsA8dNBMCSa%2B%2FoV%2FjRUE%2BsZsKWHb1z)