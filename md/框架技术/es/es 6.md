1.  字段类型<br>

|一级分类|二级分类|具体类型|
|:------:|:-----:|:-----:|
|核心类型	 |字符串类型	|text,keyword|
|            |整数类型	    |integer,long,short,byte|
|            |浮点类型	    |double,float,half_float,scaled_float|
|            |逻辑类型	    |boolean|
|            |日期类型	    |date|
|            |范围类型	    |range|
|            |二进制类型	|binary|
|复合类型	 |数组类型	    |array|
|            |对象类型	    | object|
|            |嵌套类型	     |nested|
|地理类型	 |地理坐标类型	 | geo_point|
|            |地理地图	      |geo_shape|
|特殊类型	 |IP类型	     |ip|
|            |范围类型	     |completion|
|            |令牌计数类型	|token_count|
|            |附件类型	     |attachment|
|            |抽取类型	     |percolator|
2. 分析
    1. es6不再支持`string`
    2. `keyword`不分词，只能搜索该字段的完整的值，只用于 filtering 
    3. `text`用于全文索引，该类型的字段将通过分词器进行分词，最终用于构建索引
    4. `date`由于Json没有date类型，所以es通过识别字符串是否符合format定义的格式来判断是否为date类型
        format默认为：`strict_date_optional_time||epoch_millis` format
    5.   `boolean` 现在只可以设置为 "false",  "true"   
    
3. Mapping参数     

    |属性名|说明|默认值|
    |:------:|:-----|:-----:|
    |analyzer	|分词器名称	|standard标准分词器|
    |normalizer	|统一处理方法，例如将Apple，apple，applE,统一按lowercase处理，检索时全部都能检索到|	无|
    |boost	|字段级别的助推，默认值是1，定义了字段在文档中的重要性/权重|	1|
    |coerce|	尝试将字符串转换为数值，如果字段类型是整数，那么将小数取整	|true|
    |copy_to	|该属性指定一个字段名称，ElasticSearch引擎将当前字段的值复制到该属性指定的字段中|	无|
    |doc_values	|文档值是存储在硬盘上的索引时（indexing time）数据结构，<br>对于index:"not_analyzed"字段，默认值是true，analyzed string字段不支持文档值;||	 
    |dynamic	|能够动态的增加自动，可对index，单个对象设置|	true|
    |enabled	|搜索开关，设置为false后，保存数据但是不可检索|	true|
    |fielddata	|字段数据是存储在内存中的查询时（querying time）数据结构，只支持analyzed string字段|	| 
    |eager_global_ordinals	|按照global ordinals积极把fielddata加载到内存	|true|
    |format	|指定日期的格式，例如：“yyyy-MM-dd hh:mm:ss”|	 |
    |ignore_above|	该属性值指的是字符数量，而不是字节数量；超过长度后，后面的字符将会被分析器忽略。|	256|
    |ignore_malformed	|忽略格式错误的数值，默认值是false，不忽略错误格式，对整个文档不处理，并且抛出异常	|false|
    |index_options	| index_options指出哪些信息被加到倒排索引中，<br>docs，freqs，positions，offsets，stored文档默认为positions，其他文档默认为docs||	 
    |index	|该属性控制字段是否编入索引被搜索，可以设置为true,false|	true|
    |fields	|多域，一个字段使用多种其他类型的副本数据，满足不同的搜索场景	|| 
    |norms	|用于标准化文档，以便查询时计算文档的相关性。|建议不开启 默认不开启	false|
    |null_value	|默认情况下值为null的字段不被index和search，该参数可以让值为null的字段变得可index和search	|| 
    |position_increment_gap	|多字段位置相邻度||	 
    |properties	|嵌套字段，对象的字段信息。||	 
    |search_analyzer|	搜索时使用的分析器，默认情况下，查询将使用analyzer字段制定的分析器，但也可以被search_analyzer覆盖||	 
    |similarity	|指定文档的评分模型，参数由"BM25"（默认）, "classic"（TF/IDF）, "boolean"（布尔评分模型）	|| 
    |store	|指定是否将字段的原始值写入索引，默认值是no，字段值被分析，能够被搜索，<br>但是，字段值不会存储，这意味着，该字段能够被查询，但是不会存储字段的原始值。|	no|
    |term_vector|	词条向量	|| 

    1. analyzer，用于 text 类型字段，分词产生多个 token，
    2. boost
    3. dynamic,是否允许新增字段在最外层使用。属性有`true`，`false `不允许自动新增字段，但是文档可以正常写入，但无法对新增字段进行查询等操作，`strict ` 文档不能写入，报错
    4. index    
    5. index_options<br>
       index_options参数控制将哪些信息添加到倒排索引，以用于搜索和突出显示，可选的值有：`docs`，`freqs`，`positions`，`offsets`
        1. docs：只索引 doc id
        2. freqs：索引 doc id 和词频，平分时可能要用到词频
        3. positions：索引 doc id、词频、位置，做 proximity or phrase queries 时可能要用到位置信息
        5. offsets：索引doc id、词频、位置、开始偏移和结束偏移，高亮功能需要用到offsets
    6.  fielddata,是否预加载 fielddata，默认为false。   
          1.  Elasticsearch第一次查询时完整加载这个字段所有 Segment 中的倒排索引到内存中
          2.  如果我们有一些 5 GB 的索引段，并希望加载 10 GB 的 fielddata 到内存中，这个过程可能会要数十秒
          3.  将 fielddate 设置为 true ,将载入 fielddata 的代价转移到索引刷新的时候，而不是查询时，从而大大提高了搜索体验
    7.  eager_global_ordinals，是否构建全局序号，默认false
          ```aidl
                是否在刷新的时候优先加载全局排序, 默认 `false`.
                一般会在字段频繁的用于 (significant) terms aggregations 的时候开启.
           ```
    8.  doc_values    
          ````aidl
            是否需要把字段值以 `列式` 存储到磁盘中, 以支持后续的排序聚合以及 scripting. 默认 `true`.
          ````
    9.  fields ,   该参数的目的是为了实现 multi-fields
    10. normalizer , normalizer 用于 keyword 类型，只产生一个 token（整个字段的值作为一个token，而不是分词拆分为多个token）                      
    11. enabled   是否索引，默认为 true    
    12. ignore_above
            1. 设置能被索引的字段的长度
            2.  超过这个长度，该字段将不被索引，所以无法搜索，但聚合的terms可以看到
    13.  null_value        
            1. 该字段定义遇到null值时的处理策略，默认为Null，即空值，此时ES会忽略该值
            2. 通过设定该值可以设定字段为 null 时的默认值
    14.  ignore_malformed        
            1. 当数据类型不匹配且 coerce 强制转换时,默认情况会抛出异常,并拒绝整个文档的插入
            2. 若设置该参数为 true，则忽略该异常，并强制赋值，但是不会被索引，其他字段则照常
    15.  norms        
            1.  norms 存储各种标准化因子，为后续查询计算文档对该查询的匹配分数提供依据
            2.  norms 参数对评分很有用，但需要占用大量的磁盘空间
            3.  如果不需要计算字段的评分，可以取消该字段 norms 的功能
    16.  position_increment_gap ，与 proximity queries（近似查询）和 phrase queries（短语查询）有关，默认值是100 <br>   
           [详解](https://blog.csdn.net/chuan442616909/article/details/56664861)        
    17.  search_analyzer，搜索分词器，查询时使用  ik分词器时使用
    18.  store    
            1. store 的意思是：是否在 _source 之外在独立存储一份，默认值为 false
            2. es在存储数据的时候把json对象存储到"_source"字段里，"_source"把所有字段保存为一份文档存储（读取需要1次IO），要取出某个字段则通过 source filtering 过滤
            3. 字段比较多或者内容比较多，并且不需要取出所有字段的时候，可以把特定字段的store设置为true单独存储（读取需要1次IO），同时在_source设置exclude
    19.  term_vector，与倒排索引相关
4.  分词器选择    
     [分词器比较](https://zhuanlan.zhihu.com/p/29183128)