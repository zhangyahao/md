InnoDB事务的隔离级别有四级，默认是“可重复读”（REPEATABLE READ）。   
  1.  未提交读（READ UNCOMMITTED）。另一个事务修改了数据，但尚未提交，而本事务中的SELECT会读到这些未被提交的数据（脏读）。
  2.  提交读（READ COMMITTED）。本事务读取到的是最新的数据（其他事务提交后的）。问题是，在同一个事务里，前后两次相同的SELECT会读到不同的结果（不重复读）。
  3.  可重复读（REPEATABLE READ）。在同一个事务里，SELECT的结果是事务开始时时间点的状态，因此，同样的SELECT操作读到的结果会是一致的。但是，会有幻读现象（稍后解释）。
  4.  串行化（SERIALIZABLE）。读操作会隐式获取共享锁，可以保证不同事务间的互斥。  
  
四个级别逐渐增强，每个级别解决一个问题。  
  1.  脏读，最容易理解。另一个事务修改了数据，但尚未提交，而本事务中的SELECT会读到这些未被提交的数据。
  2.  不重复读。解决了脏读后，会遇到，同一个事务执行过程中，另外一个事务提交了新数据，因此本事务先后两次读到的数据结果会不一致。
  3.  幻读。解决了不重复读，保证了同一个事务里，查询的结果都是事务开始时的状态（一致性）。但是，如果另一个事务同时提交了新数据，本事务再更新时，就会“惊奇的”发现了这些新数据，貌似之前读到的数据是“鬼影”一样的幻觉。


当隔离级别是可重复读，且禁用innodb_locks_unsafe_for_binlog的情况下，在搜索和扫描index的时候使用的next-key locks可以避免幻读。 
next-key locks需要应用程序自己去加锁。   
MySQL InnoDB的可重复读并不保证避免幻读，需要应用使用加锁读来保证。而这个加锁度使用到的机制就是next-key locks。但是next-key locks会使普通读加锁。   
重复读和提交读是矛盾的。在同一个事务里，如果保证了可重复读，就会看不到其他事务的提交，违背了提交读；如果保证了提交读，就会导致前后两次读到的结果不一致，违背了可重复读。

[原文](https://mp.weixin.qq.com/s?__biz=MzIzMzgxOTQ5NA==&mid=2247496794&idx=2&sn=9608df29393fdccb0b25da75ac522813&chksm=e8fd6853df8ae1454122ffc89647622ad6cd2ce1386ac968f469864a602b18865428b97540cf&mpshare=1&scene=1&srcid=0706ixdAd9048gZfpgO9NZFt&sharer_sharetime=1593999127493&sharer_shareid=7fccbb6e38b69cfc35054be7928be058&key=7b0bb172d3b89960799e5ce5aa1a4d56b61bc86ca54d095931136eaadb1ec49f499dc92aede45e4a4f69df64c1201cb683219d3c0f544628c64aea4f586bb53c1cfb32467688420b8d8b2e85993a4a8a&ascene=1&uin=MjUwNzEwMTIyMg%3D%3D&devicetype=Windows+10+x64&version=62090529&lang=zh_CN&exportkey=AfUPsO9dkg5jJi1w7Q0YSUA%3D&pass_ticket=bFnrkf%2F1jWK6o5TPAIfH6luXazVjOx4kzGbZTx23qyMXR%2B7n5T3HO8M068l5iCdV)

 