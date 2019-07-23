1.  jar
    ```$xslt
         <dependency>
                    <groupId>org.springframework.kafka</groupId>
                    <artifactId>spring-kafka</artifactId>
                    <version>2.2.0.RELEASE</version>
                </dependency>

    ```
2.  工具类
    ```$xslt
        package com.cmcc.vmpp.base;
        
        import org.apache.kafka.clients.consumer.ConsumerConfig;
        import org.apache.kafka.common.serialization.StringDeserializer;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.kafka.annotation.EnableKafka;
        import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
        import org.springframework.kafka.config.KafkaListenerContainerFactory;
        import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
        
        import java.util.HashMap;
        import java.util.Map;
        
        /**
         * @program: vmpp
         * @description: kafka消息处理
         * @author: Zhang
         * @create: 2019-06-18 17:10
         **/
        @Configuration
        @EnableKafka
        public class MessageProcessing {
            private static Logger logger = LoggerFactory.getLogger(MessageProcessing.class);
        
            @Value("${spring.kafka.consumer.group-id}")
            String groupId;
        
            @Value("${spring.kafka.bootstrap-servers}")
            String bootstrapServers;
        
            @Value("${spring.kafka.consumer.group-id2}")
            String groupId2;
            @Bean
            KafkaListenerContainerFactory<?> batchFactory() {
                ConcurrentKafkaListenerContainerFactory<String, String> factory = new
                        ConcurrentKafkaListenerContainerFactory<>();
                Map<String, Object> config = consumerConfigs();
                config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
                factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(config));
                //设置并发量
        //        factory.setConcurrency(5);
        //         开启批量监听
                factory.setBatchListener(true);
                return factory;
            }
            @Bean
            KafkaListenerContainerFactory<?> reviewFactory() {
                ConcurrentKafkaListenerContainerFactory<String, String> factory = new
                        ConcurrentKafkaListenerContainerFactory<>();
                Map<String, Object> config = consumerConfigs();
                config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId2);
                factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(config));
                //设置并发量
        //        factory.setConcurrency(5);
        //         开启批量监听
                factory.setBatchListener(true);
                return factory;
            }
            @Bean
            public Map<String, Object> consumerConfigs() {
                Map<String, Object> props = new HashMap<>();
                props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
                props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100); //设置每次接收Message的数量
                props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
                props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 120000);
                props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 180000);
                props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "104857600");
                return props;
            }
        }

    ```
3.  监听    
    ```$xslt
         @KafkaListener(topics = "topic", containerFactory = "batchFactory")
         public void batchListener(List<String> data) {
            .....
         }
    ```