###原因
Insert into a select * from b where xxxx=xxxx   ;  
在默认的事务隔离级别下，该语句加锁规则是是：a表锁，b 为逐步锁，扫描一条锁一条。  
###解决办法  
由于查询条件会导致b全表扫描，因此给b表条件字段加索引。
