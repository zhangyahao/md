###REDIS提供了两种持久化的方式
1. ***RDB***
2. ***AOF***
3. 在redis 4.0 中提供了两者同时使用的混合持久化  

   1. 使用 `info persistence`可以查看所有的相关的持久化信息<br>
        ![图片](https://mmbiz.qpic.cn/mmbiz_png/NNss032FISg9BsFxhiaJt3Y7rkPVibkgEDXG8XIXnP0reOa3Xzic0k4x3iaaAoeypaHhIREsia8CXia5GwjuECOYpniaA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)


##RDB       
  1. 原理<br>
      RDB持久化是通过`快照`方式来完成的。当达到触发条件时，Redis会自动将`内存`中`所有数据`以`二进制`方式生成一份副本并存储在硬盘上
      在配置文件可以配置当前配置的备份文件和目录，使用`config`命令也可以查看和设置：
      ![图片](https://mmbiz.qpic.cn/mmbiz_png/NNss032FISg9BsFxhiaJt3Y7rkPVibkgEDOTMmAHX9A02nbOhxvzpUD4Wbia925nRlInc1wkZOBEakrBHy8F6rdNA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
  2.  触发条件<br>    
       RDB分为`主动触发`和`被动触发`。<br>
      1.  主动触发指的是客户端执行`save`和`bgsave`命令会进行持久化。<br>
           执行save会使Redis处于阻塞状态，不会响应任何其他客户端发来的请求，直到RDB快照文件执行完毕，需要谨慎使用。<br>
           bgsave即background save，后台保存。当执行bgsave命令时，Redis会fork出一个子进程来执行快照操作。需要注意的是，<br>
           在fork子进程的过程中，Redis是阻塞的。而当子进程创建完成后，Redis就可以继续响应客户端的请求了。 
      2.  被动触发
            1. save m n规则触发<br>
                在指定的m秒内，Redis中有n个键发生改变，则自动触发bgsave。该规则默认在redis.conf中进行了配置，并且可组合使用，满足其中一个规则，则触发bgsave
            2. flushall触发 <br>    
                flushall命令用于清空数据库，请慎用，当我们使用了则表明我们需要对数据进行清空，那Redis当然需要对快照文件也进行清空，所以会触发bgsave。
            3. shutdown触发<br>    
               Redis在关闭前处于安全角度将所有数据全部保存下来，以便下次启动会恢复。可以使用客户端连入执行shutdown命令，也可以直接使用脚本关闭Redis，都会在退出前先执行save。
                ```aidl
                  shutdown命令还可以传递一个参数save/nosave。如果使用nosave参数，则不会进行持久化，直接退出。
                ```
            4.  主从复制触发<br>    
                在Redis主从复制中，从节点执行全量复制操作，主节点会执行bgsave命令，并将rdb文件发送给从节点。
            
            5.  配置<br>
                    ![配置](https://mmbiz.qpic.cn/mmbiz_png/NNss032FISg9BsFxhiaJt3Y7rkPVibkgEDS5ic9ENfWPIPFBcbXTHskN3JFiaV2Ll6RfJAELsAwVyLS3KG8ibkzQ8nQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
##AOF
  1.  介绍<br>
        默认情况下，Redis是关闭了AOF持久化，开启AOF通过配置appendonly为yes开启，修改配置文件或者在命令行直接使用config set<br>
        修改，在用config rewrite同步到配置文件。通过客户端修改好处是不用重启Redis，AOF持久化直接生效。
  2.  原理<br>      
        如果说RDB相当于数据库的定时备份（冷备），那AOF就相当于数据库的热备。  
        通过前面的介绍会发现，如果使用RDB，在意外情况下，比如Redis服务突然宕机，  
        这个时候有些内存里面的数据还没来得及刷新进磁盘，就会导致一部分数据丢失。  
          
        AOF就是用来解决这个问题的。AOF是Append Only File的缩写，AOF可以将Redis执行的每一条写命令追加到磁盘文件中，  
        在Redis启动时候优先选择从AOF文件恢复数据。  
          
        因为每一次写操作，都需要记录到文件中，所以开启AOF持久化会对性能有一定的影响，  
        但是大部分情况下这个影响是可以接受的，我们可以使用读写速率高的硬盘提高AOF性能。  
        与RDB持久化相比，AOF持久化数据丢失更少，其消耗内存更少(RDB方式执行bgsve会有内存拷贝)。  
  3.  持久化过程  
        1. 追加写入  
            Redis将每一条写命令以Redis通讯协议添加至缓冲区aof_buf,这样的好处在于在大量写请求情况下，采用缓冲区  
            暂存一部分命令随后根据策略一次性写入磁盘，这样可以减少磁盘的I/O次数，提高性能。  
        2.  同步命令到硬盘  
            当写命令写入aof_buf缓冲区后，Redis会将缓冲区的命令写入到文件，Redis提供了三种同步策略，  
            由配置参数appendfsync决定，下面是每个策略所对应的含义： 
                1. no：不使用fsync方法同步，而是交给操作系统write函数去执行同步操作，在linux操作系统中大约每30秒刷一次缓冲。
                2. always：表示每次有写操作都调用fsync方法强制内核将数据写入到aof文件。
                3. everysec：数据将使用调用操作系统write写入文件，并使用fsync每秒一次从内核刷新到磁盘。  
                   这是折中的方案，兼顾性能和数据安全，所以Redis默认推荐使用该配置。         
        3.  文件重写(bgrewriteaof)  
            当开启的AOF时，随着时间推移，AOF文件会越来越大,当然Redis也对AOF文件进行了优化，  
            即触发AOF文件重写条件的时候，Redis将使用bgrewriteaof对AOF文件进行重写。这样的好处  
            在于减少AOF文件大小，同时有利于数据的恢复。重写策略：     
              1. 重复或无效的命令不写入文件
              2. 过期的数据不再写入文件
              3. 多条命令合并写入（当多个命令能合并一条命令时候会对其优化合并作为一个命令写入，  
                  例如“RPUSH list1 a; RPUSH list1 b" 合并为“RPUSH list1 a b” ）      
        4.  重写            
            AOF文件重写过程与RDB快照bgsave工作过程有点相似，都是通过fork子进程，由子进程完成相应的操作，同样的在  
            fork子进程简短的时间内，Redis是阻塞的，过程如图：
            ![重写](https://mmbiz.qpic.cn/mmbiz_png/NNss032FISg9BsFxhiaJt3Y7rkPVibkgEDYHUToESSDtMmjuicFRsGhXrZMELLG8znibznX2ba3x1D2GH3MkMDkytw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
        5.  重写的触发条件：  
              手动触发：客户端执行bgrewriteaof命令。
              
              自动触发：自动触发通过以下两个配置协作生效： 
                 1.  auto-aof-rewrite-min-size: AOF文件最小重写大小，只有当AOF文件大小大于该值时候才可能重写，4.0默认配置64mb。
                 2.  auto-aof-rewrite-percentage：当前AOF文件大小和最后一次重写后的大小之间的比率等于或者等于指定的增长百分比，  
                      如100代表当前AOF文件是上次重写的两倍时候才重写。
                      
              Redis开启在AOF功能开启的情况下，会维持以下三个变量
                  1.   记录当前AOF文件大小的变量aof_current_size。            
                  2.   记录最后一次AOF重写之后，AOF文件大小的变量aof_rewrite_base_size。
                  3.   增长百分比变量aof_rewrite_perc。   
              每次当serverCron（服务器周期性操作函数）函数执行时，它会检查以下条件是否全部满足，如果全部满足的话，   
              就触发自动的AOF重写操作：    
                  1.  没有BGSAVE命令（RDB持久化）/AOF持久化在执行
                  2.  没有BGREWRITEAOF在进行
                  3.  当前AOF文件大小要大于server.aof_rewrite_min_size的值
                  4.  当前AOF文件大小和最后一次重写后的大小之间的比率等于或者大于指定的增长百分比（auto-aof-rewrite-percentage参数）
        6.   数据恢复
                 当AOF开启时候，Redis数据恢复优先选用AOF进行数据恢复。关掉Redis服务再重启，会发现日志里面有： 
                 
                 DB loaded from append only file
##RDB与AOF对比                  
  1.  RDB的优点：  
        RDB文件体积小，因此在传输速度上较快，适合灾难恢复。RDB使Redis性能更高，子进程处理保存工作。在恢复大数据集时比AOF恢复速度快。
  2.  AOF的优点： 
        数据更完整和安全，秒级数据丢失（取决于appendfsync策略）。兼容性高，因为是基于Redis通讯协议形成的明文文件，  
        所以容易阅读，且任何版本的Redis都兼容。          
##混合持久化        
  简单的说：新的AOF文件前半段是RDB格式的全量数据，后半段是AOF格式的增量数据。
  1.  数据恢复  
       1.  AOF文件开头是RDB的格式，先加载RDB部分的内容，再加载剩余的AOF  
       2.  AOF文件开头不是RDB的格式，直接加载整个AOF文件
  2.  配置  
       4.0版本的混合持久化默认关闭的，通过`aof-use-rdb-preamble`配置参数控制，`yes`则表示开启，`no`表示禁用，默认是no，  
       可通过`config set`修改。