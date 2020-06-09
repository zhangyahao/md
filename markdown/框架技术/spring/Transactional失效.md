1. @Transactional 应用在非 public 修饰的方法上
2. @Transactional 注解属性 propagation 设置错误
3. @Transactional 注解属性 rollbackFor 设置错误.  
   rollbackFor 可以指定能够触发事务回滚的异常类型。Spring默认抛出了未检查unchecked异常（继承自 RuntimeException 的异常）或者 Error才回滚事务；其他异常不会触发回滚事务。如果在事务中抛出其他类型的异常，但却期望 Spring 能够回滚事务，就需要指定rollbackFor属性。
4. 同一个类中方法调用，导致@Transactional失效   
5. 异常被你的 catch“吃了”导致@Transactional失效   
6.  数据库引擎不支持事务



[原文](https://zhuanlan.zhihu.com/p/145897825)
