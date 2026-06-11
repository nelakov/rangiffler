package com.elakov.rangiffler.test.kafka;

import com.elakov.rangiffler.data.repository.userdata.UserdataRepository;
import com.elakov.rangiffler.data.repository.userdata.UserdataRepositoryImpl;
import com.elakov.rangiffler.helper.data.DataFakeHelper;
import com.elakov.rangiffler.jupiter.annotation.meta.Env;
import com.elakov.rangiffler.jupiter.annotation.meta.KafkaTest;
import com.elakov.rangiffler.kafka.UserMessage;
import com.elakov.rangiffler.kafka.UsersKafkaClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Kafka")
@Feature("User registration events")
@Tag("KAFKA")
@KafkaTest
@Env(enabledFor = {"local", "docker"})
class UserdataKafkaConsumerTest {

    private final UsersKafkaClient usersKafkaClient = new UsersKafkaClient();
    private final UserdataRepository userdataRepository = new UserdataRepositoryImpl();

    @Test
    @DisplayName("kafka: message in `users` topic creates user row in userdata")
    void messageShouldBeConsumedFromKafkaAndUserCreatedInUserdata() {
        String username = DataFakeHelper.generateRandomUsername();

        usersKafkaClient.sendUserToTopic(new UserMessage(username));

        var dbUser = Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofSeconds(1))
                .ignoreExceptions()
                .until(
                        () -> userdataRepository.findByUsername(username),
                        Objects::nonNull
                );

        assertThat(dbUser.getUsername()).isEqualTo(username);
        assertThat(dbUser.getFirstname()).isNull();
        assertThat(dbUser.getSurname()).isNull();
    }
}
