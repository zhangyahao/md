1. jedis
   ```$xslt
    Set<String> strings = jedis.keys("vod_*");
    Set<String> strings = jedis.keys("vod_?");
    ```
    1. `?`1个字符
    2. `*`0到任意多个字符