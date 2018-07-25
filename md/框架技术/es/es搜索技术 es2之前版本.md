#####ElasticSearch（luncene）

1. 简介
    是一种无模式的搜索引擎 
    1. 官方定义为映射结构
    2. 个人认为类似于数据库(MongoDB) 将数据建立index放入其中 
    3. 面向文档
    
2. 依赖  org.ElasticSearch

3.  核心类型
    1.  string：字符串 
    2.  number：byte, short, integer, long数字
    3.  date：日期
    4.  boolean：布尔
    5.  binary：二进制
    6.  range：范围值 `integer_range, float_range, long_range, double_range, date_range`
    
4.  公共属性
     1.  index_name:定义属性存储的索引的字段名称
     2.  index：属性有 analyzed和no.如果是字符串字段还可以设置not_analyzed.
          anaylzed是可被搜索到，no为否.**如果是字符串设置为 not_analyzed则该字段
          将直接编入索引，搜索该字段就必须全部匹配**
     3.  boost：权重 默认为1 越大该字段的权重越大.
     4.  nullvalue ：该字段不是索引的一部分.写入索引后，默认忽略.
     5.  include_in_all：此属性指定该字段是否应包括在_all字段中。默认情况下，如果使用_all字段，所有字段都会包括
            当**_index设置为no，此属性失效_**
     6.  store：布尔值 也可以是 yes或者no 指定该字段的原始值是否写入索引.默认是no.如果是no 结果中将不返回该字段
     7.  ik：中文分词器  是第三方的插件 同时还有 mmsegf分词器和paoding分词器.已移植到es中.
                1.  安装ik分词器到elasticsearch很简单，它有个插件目录analysis-ik，和一个配置目录ik, 
                分别拷贝到plugins和conf目录就可以了。当然你可以使用elasticsearch的plugin命令去安装，这个过程可能会有些麻烦。
                2.  ik_max_word：会将文本做最细粒度的拆分，例如「中华人民共和国国歌」会被拆分为「中华人民共和国、中华人民、
                中华、华人、人民共和国、人民、人、民、共和国、共和、和、国国、国歌」，会穷尽各种可能的组合； 
                    ik_smart：会将文本做最粗粒度的拆分，例如「中华人民共和国国歌」会被拆分为「中华人民共和国、国歌」；
     8.   multi-field： 多域类型允许你对同一个值以映射的方式定义成多个基本类型  
     8.   示例 <br>
            [索引建立方式](https://blog.csdn.net/napoay/article/details/51707023)
            ```
                //mysqldao 中查询所有的es 的index的信息
                                        mysqldao  mysqldao =new mysqldao();
                                        Client client = this.esu.getClient();
                                        XContentBuilder mapping = null;
                                        mapping = jsonBuilder().startObject()
                    			        		  .startObject("properties")
                    			        		           .startObject("mzName").field("type", "string").field("index_analyzer","ik").field("search_analyzer","ik_smart").field("store",true).endObject()
                                          PutMappingRequest mappingRequest = Requests.putMappingRequest("businesshall").type("yyt").source(mapping);
                     		               //创建索引
                                           client.admin().indices().putMapping(mappingRequest).actionGet();    
           ```
                      
     9.   查询   
            [聚合索引使用方法](https://elasticsearch.cn/article/102) 
            对特殊字符的处理
            [具体操作方式](https://blog.csdn.net/dlm_bk/article/details/79758413)           
          ```aidl
                  public HashSet<String> getB(){
                       		HashSet<String> zhiboIdSet = null;
                       		Client client = new Client();
             		
   		             //中文搜索 模糊查询
             		QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder("\""+kwords+"\"");
             		//引用ik分析器查询相应字段
   		             queryBuilder.quoteAnalyzer("ik").field("mzName");
   		
                       		//搜索时的字段
                       		QueryBuilder qb = QueryBuilders.queryString("*").field("name") .minimumShouldMatch("100%");  //设置最小的匹配度
                            // boolQuery 搜索
                            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();  
                                  //should相当于  或 可以是多条
                                      boolQuery.should(QueryBuilders.termQuery("a", a));                                                                   								   
                                      boolQuery.should(QueryBuilders.termQuery("a", b));                                                                   								   
                                      boolQuery.should(QueryBuilders.termQuery("a", c));                                                                   								   
                                  //设置包含其中的几条
                                      //最少包含的条数
                                       boolQuery.minimumNumberShouldMatch(2);
                                     //must  必须  相当于 and
                                          boolQuery.must(QueryBuilders.termQuery("a", a));
                                          boolQuery.must(QueryBuilders.termQuery("a", b));
                                          boolQuery.must(QueryBuilders.termQuery("a", c));
                                          //不包含
                                          boolQuery.mustNot(QueryBuilders.termQuery("a", c));
                                  QueryBuilder qb =    boolQuery;     
                       		//搜索的具体索引  b  类型yyt
                       		SearchResponse response = client.prepareSearch("b").setTypes("yyt")
                       		                            //搜索的关键字
                       									.setQuery(qb)
                       									//每次搜索的范围
                       									.setFrom(0).setSize(10000)
                       									//查询匹配的字段 第一参数为包含  第二个为排除
                       								    .setFetchSource("mzName", null)
             								            //使用min聚合查询某个字段上最小的值。
             								            .addAggregation(AggregationBuilders.min("min").field("age"))
                       								    //设置最小的匹配度
                       								    .minimumShouldMatch("100%")
                       								    //设置过滤
                       								    .setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18))
             								            //排序   .missing("_last"))不能应用于布尔值的索引
   								                        .addSort(new FieldSortBuilder("id").order(SortOrder.DESC).missing("_last"))
	                                                     //或者这样排序
	                                                    .addSort("id",SortOrder.DESC)
                                                          //或者
                                                        .addSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))    
                       								    //查询全部
                       								    .setExplain(true)
                       								    .execute().actionGet(); 
                       		//单次获取的结果
                       		SearchHits hits = response.getHits(); 
             		        CommonSearchResult csr = null;
                       		//遍历结果
                       		if(hits.getTotalHits()>0){
             		             csr = new CommonSearchResult();
                       			 csr.setTotalNum((int) hits.getTotalHits());
                                            this.setResult(csr, hits, name);
                       		}else{
             		            csr = new CommonSearchResult();
                                csr.setTotalNum(0);
             		         }
                       		response = null;
                       		hits = null;
                       		return csr;
                       	}

         ```
