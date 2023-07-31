//package com.elakov.rangiffler.config;
//
//import com.elakov.rangiffler.model.UserJson;
//import com.fasterxml.jackson.databind.JsonSerializable;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.kafka.config.TopicBuilder;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
//import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
//
//@Configuration
//public class RangifflerAuthProducerConfiguration {
//
//    private final KafkaProperties kafkaProperties;
//
//    @Autowired
//    public RangifflerAuthProducerConfiguration(KafkaProperties kafkaProperties) {
//        this.kafkaProperties = kafkaProperties;
//    }
//
//    @Bean
//    public Map<String, Object> producerConfiguration() {
//        Map<String, Object> properties = new HashMap<>(kafkaProperties.buildProducerProperties());
//        properties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        properties.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return properties;
//    }
//
//    @Bean
//    public ProducerFactory<String, UserJson> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(producerConfiguration());
//    }
//
//    @Bean
//    public KafkaTemplate<String, UserJson> kafkaTemplate() {
//        return new KafkaTemplate<String, UserJson>(producerFactory());
//    }
//
//    @Bean
//    @Primary
//    public NewTopic topic() {
//        return TopicBuilder.name("users")
//                .partitions(10)
//                .replicas(1)
//                .build();
//    }
//}
