1.  TransportClient  
    此连接方式在es2 之后官方已不建议使用。至es7彻底废弃。 连接9300 端口。
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
3.  详情
    1.     RestHighLevelClient，此方式比较适合复杂查询。普通操作也可以使用。
    