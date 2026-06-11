package com.elakov.rangiffler.service;

import com.elakov.rangiffler.model.UserJson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaUserService {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaUserService.class);

    private final UserDataService userDataService;

    public KafkaUserService(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @KafkaListener(topics = "users", groupId = "userdata")
    public void listener(@Payload UserJson user, ConsumerRecord<String, UserJson> record) {
        LOG.info("### Kafka topic [users] received message: {} (partition={}, offset={})",
                user.username(), record.partition(), record.offset());
        UserJson saved = userDataService.getCurrentUserOrCreateIfAbsent(user.username());
        LOG.info("### User {} is present in userdata with id {}", saved.username(), saved.id());
    }
}
