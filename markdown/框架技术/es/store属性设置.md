  [ 转载](https://www.cnblogs.com/Hai--D/p/5761525.html)


众所周知_source字段存储的是索引的原始内容，那store属性的设置是为何呢？es为什么要把store的默认取值设置为no？设置为yes是否是重复的存储呢？ 

 

我们将一个field的值写入es中，要么是想在这个field上执行search操作（不知道具体的id），要么执行retrieve操作（根据id来 检索）。但是，如果不显式的将该field的store属性设置为yes，同时_source字段enabled的情况下，你仍然可以获取到这个 field的值。这就意味着在一些情况下让一个field不被index或者store仍然是有意义的。 

 

当你将一个field的store属性设置为true，这个会在lucene层面处理。lucene是倒排索引，可以执行快速的全文检索，返回符合检索条 件的文档id列表。在全文索引之外，lucene也提供了存储字段的值的特性，以支持提供id的查询（根据id得到原始信息）。通常我们在lucene层 面存储的field的值是跟随search请求一起返回的（id+field的值）。es并不需要存储你想返回的每一个field的值，因为默认情况下每 一个文档的的完整信息都已经存储了，因此可以跟随查询结构返回你想要的所有field值。 

 

有一些情况下，显式的存储某些field的值是必须的：当_source被disabled的时候，或者你并不想从source中parser来得到 field的值（即使这个过程是自动的）。请记住：从每一个stored field中获取值都需要一次磁盘io，如果想获取多个field的值，就需要多次磁盘io，但是，如果从_source中获取多个field的值，则只 需要一次磁盘io，因为_source只是一个字段而已。所以在大多数情况下，从_source中获取是快速而高效的。 

 

es中默认的设置_source是enable的，存储整个文档的值。这意味着在执行search操作的时候可以返回整个文档的信息。如果不想返回这个文 档的完整信息，也可以指定要求返回的field，es会自动从_source中抽取出指定field的值返回（比如说highlighting的需求）。 

 

你可以指定一些字段store为true，这意味着这个field的数据将会被单独存储。这时候，如果你要求返回field1（store：yes），es会分辨出field1已经被存储了，因此不会从_source中加载，而是从field1的存储块中加载。  
 
哪些情形下需要显式的指定store属性呢？大多数情况并不是必须的。从_source中获取值是快速而且高效的。如果你的文档长度很长，存储 _source或者从_source中获取field的代价很大，你可以显式的将某些field的store属性设置为yes。缺点如上边所说：假设你存 储了10个field，而如果想获取这10个field的值，则需要多次的io，如果从_source中获取则只需要一次，而且_source是被压缩过 的。 
 
还有一种情形：reindex from some field，对某些字段重建索引的时候。从source中读取数据然后reindex，和从某些field中读取数据相比，显然后者代价更低一些。这些字段store设置为yes比较合适。
  

总结：
 如果对某个field做了索引，则可以查询。如果store：yes，则可以展示该field的值。

 但是如果你存储了这个doc的数据（_source enable），即使store为no，仍然可以得到field的值（client去解析）。

 所以一个store设置为no 的field，如果_source被disable，则只能检索不能展示。