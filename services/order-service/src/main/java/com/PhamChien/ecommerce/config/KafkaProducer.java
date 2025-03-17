package com.PhamChien.ecommerce.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import java.util.HashMap;
import java.util.Map;


@Configuration
@Slf4j
public class KafkaProducer {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.topic.orderCreated}")
    private String orderCreated;

    @Value("${spring.kafka.topic.orderUpdated}")
    private String orderUpdated;

    @Value("${spring.kafka.topic.orderMail}")
    private String orderMail;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        log.debug("Creating producer factory");
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        log.debug("Creating Kafka template");
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic(orderCreated, 3, (short) 1);
    }
    @Bean
    public NewTopic topic2() {
        return new NewTopic(orderUpdated, 3, (short) 1);
    }
    @Bean
    public NewTopic topic3() {
        return new NewTopic(orderMail, 3, (short) 1);
    }
}
