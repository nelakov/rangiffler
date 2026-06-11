package com.elakov.rangiffler.service;

import com.elakov.rangiffler.model.UserJson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaUserServiceTest {

    @Mock
    private UserDataService userDataService;
    @InjectMocks
    private KafkaUserService kafkaUserService;

    @Test
    @DisplayName("listener delegates the consumed username to getCurrentUserOrCreateIfAbsent")
    void listenerCreatesUser() {
        UserJson payload = new UserJson(null, "bob", null, null, null, null);
        when(userDataService.getCurrentUserOrCreateIfAbsent("bob"))
                .thenReturn(new UserJson(UUID.randomUUID(), "bob", null, null, null, null));
        var record = new ConsumerRecord<String, UserJson>("users", 0, 0L, null, payload);

        kafkaUserService.listener(payload, record);

        verify(userDataService).getCurrentUserOrCreateIfAbsent("bob");
    }
}
