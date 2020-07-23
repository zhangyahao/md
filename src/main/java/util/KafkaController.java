package util;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: risk_rating
 * @description: kafka创建topic
 * @author: Zhang
 * @create: 2020-07-23 09:49
 **/
@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {
    @Value("${spring.kafka.bootstrap-servers}")
    String bootstrapServers;

    /**
     *  使用时打开，平时建议注释。防止误操作，或不安全
     * @param topic :topic
     * @param partition:分区。 分区可固定。
     * @param copy：副本
     * @return
     */
//    @GetMapping({"/topic/{topic}/{partition}/{copy}"})
    public String createTopice(@PathVariable("topic") String topic, @PathVariable("partition") int partition,
                               @PathVariable("copy") short copy) {
        try {
            Map<String, Object> configs = new HashMap<>();
            configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            //kafka 管理类
            KafkaAdmin admin = new KafkaAdmin(configs);
            //topic创建  注意：分区数量只能增加不能减少
            NewTopic newTopic = new NewTopic(topic, partition, copy);
            //kafka客户端
            AdminClient adminClient = AdminClient.create(admin.getConfig());

            List<NewTopic> topicList = Arrays.asList(newTopic);
            adminClient.createTopics(topicList);
            adminClient.close(10, TimeUnit.SECONDS);
            return "suc";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fai";

    }

}
