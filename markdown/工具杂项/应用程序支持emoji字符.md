1. 数据库要求 
    mysql<br>
     需要升级到5.5.3或更新的版本，然后，把默认编码从原来的utf8改为utf8mb4，在my.cnf或者my.ini配置文件中修改如下：
     ```aidl
       [client]
       default-character-set = utf8mb4
       
       [mysqld]
       character-set-server = utf8mb4
       collation-server     = utf8mb4_general_ci

    ```
     重启MySQL，然后使用以下命令查看编码，应该全部为utf8mb4（character_set_filesystem和character_set_system除外）：
     
            mysql> show variables like '%char%';
            +--------------------------+--------------------------+
            | Variable_name            | Value                    |
            +--------------------------+--------------------------+
            | character_set_client     | utf8mb4                  |
            | character_set_connection | utf8mb4                  |
            | character_set_database   | utf8mb4                  |
            | character_set_filesystem | binary                   |
            | character_set_results    | utf8mb4                  |
            | character_set_server     | utf8mb4                  |
            | character_set_system     | utf8                     |
            | character_sets_dir       | /usr/local/mysql-5.7.... |
            +--------------------------+--------------------------+
            8 rows in set (0.00 sec)
            
     如果character_set_database还是为utf8，需要重启服务器。
     
     
     
     
2. web软件
    要支持emoji，需要Web软件也支持。目前，已知支持emoji的包括：
       1.  java8
       2.  Node

        
 