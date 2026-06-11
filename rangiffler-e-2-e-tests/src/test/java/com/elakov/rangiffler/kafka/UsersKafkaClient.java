package com.elakov.rangiffler.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UsersKafkaClient {

    private static final String USERS_TOPIC = "users";
    // userdata's JacksonJsonDeserializer resolves the payload class from this header
    private static final String TYPE_ID_HEADER = "__TypeId__";
    private static final String USER_JSON_FQCN = "com.elakov.rangiffler.model.UserJson";

    private final ObjectMapper mapper = new ObjectMapper();

    public UserMessage getRegisteredUser(String username) {
        var record = KafkaReader.getMessageByPredicate(USERS_TOPIC, r -> r.value().contains(username));
        try {
            return mapper.readValue(record.value(), UserMessage.class);
        } catch (IOException e) {
            throw new IllegalStateException("Can't parse kafka message: " + record.value(), e);
        }
    }

    public void sendUserToTopic(UserMessage user) {
        try {
            String userJson = mapper.writeValueAsString(user);
            var typeHeader = new RecordHeader(TYPE_ID_HEADER, USER_JSON_FQCN.getBytes(StandardCharsets.UTF_8));
            KafkaWriter.sendRecordToTopic(new ProducerRecord<>(
                    USERS_TOPIC, (Integer) null, (String) null, userJson, List.of(typeHeader)));
        } catch (IOException e) {
            throw new IllegalStateException("Can't serialize kafka message for user: " + user.username(), e);
        }
    }
}
