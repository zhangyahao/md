1.  TransportClient  
    此连接方式在es2 之后官方已不建议使用。至es8彻底废弃。 连接9300 端口。
2.  RestHighLevelClient     
    spring data 提供了RestHighLevelClient 连接方式。 连接9200端口。 支持对低版本为 6.3以上
    ```java
        import org.elasticsearch.client.RestHighLevelClient;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.data.elasticsearch.client.ClientConfiguration;
        import org.springframework.data.elasticsearch.client.RestClients;
        import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
        
        /**
         * @program: springdata_es
         * @description:
         * @author: Zhang
         * @create: 2020-12-24 17:06
         **/
        @Configuration
        public class RestClientConfig   extends AbstractElasticsearchConfiguration {
        
            @Override
            public RestHighLevelClient elasticsearchClient() {
        
                final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                                                                                   .connectedTo("localhost:9200")
                                                                                   .withBasicAuth("","")
                                                                                   .build();
        
                return RestClients.create(clientConfiguration).rest();
            }
        }

    ```
3.  接口方式连接    
     适合面向对象操作es。
     1.   需要添加配置
          ```yaml
               data:
                  elasticsearch:
                    cluster-name: elasticsearch
                    cluster-nodes: 127.0.0.1:9300
          ```
     2.   添加配置类     
           ```java
              @Configuration
              public class ESConfig {
                  @PostConstruct
                  public void init(){
                      System.setProperty("es.set.netty.runtime.available.processors","false");
                  }
              }
           ```
     2.    添加接口
           ```java
               public interface EmpRepository extends ElasticsearchRepository<Emp, String> {
               
               }
           ```
           注：
            1.  名称随意，只需继承`ElasticsearchRepository`即可
3.  详情
    1.     RestHighLevelClient，此方式比较适合复杂查询。普通操作也可以使用。更像是java原生操作es
    2.     接口操作更简单。