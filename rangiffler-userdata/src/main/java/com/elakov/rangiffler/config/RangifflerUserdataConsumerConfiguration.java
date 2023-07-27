package com.elakov.rangiffler.config;

import com.elakov.rangiffler.model.UserJson;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class RangifflerUserdataConsumerConfiguration {

    private final KafkaProperties kafkaProperties;

    public RangifflerUserdataConsumerConfiguration(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ConsumerFactory<String, UserJson> consumerFactory() {
        final JsonDeserializer<UserJson> jsonJsonDeserializer = new JsonDeserializer<>();
        jsonJsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildProducerProperties(),
                new StringDeserializer(),
                jsonJsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserJson> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserJson> concurrentKafkaListenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
        return concurrentKafkaListenerContainerFactory;
    }
}
