package com.PhamChien.ecommerce.configuration;

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
@Slf4j(topic = "KAFKA-PRODUCER")
public class KafkaProducer {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.topic.mailRegister}")
    private String mailRegister;

    @Value("${spring.kafka.topic.forgotPassword}")
    private String forgotPassword;


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
        log.debug("Creating new topic");
        return new NewTopic(mailRegister, 3, (short) 1);
    }

    @Bean
    public NewTopic topic2() {
        log.debug("Creating new topic");
        return new NewTopic(forgotPassword, 3, (short) 1);
    }
}
