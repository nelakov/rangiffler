package com.elakov.rangiffler.test.kafka;

import com.elakov.rangiffler.api.rest.auth.AuthClient;
import com.elakov.rangiffler.api.rest.auth.AuthRestClient;
import com.elakov.rangiffler.helper.data.DataFakeHelper;
import com.elakov.rangiffler.jupiter.annotation.meta.Env;
import com.elakov.rangiffler.jupiter.annotation.meta.KafkaTest;
import com.elakov.rangiffler.kafka.UsersKafkaClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Kafka")
@Feature("User registration events")
@Tag("KAFKA")
@KafkaTest
@Env(enabledFor = {"local", "docker"})
class AuthKafkaProducerTest {

    private final AuthClient authClient = new AuthRestClient();
    private final UsersKafkaClient usersKafkaClient = new UsersKafkaClient();

    @Test
    @DisplayName("kafka: successful registration publishes user message to `users` topic")
    void messageShouldBeProducedToKafkaAfterSuccessfulRegistration() {
        String username = DataFakeHelper.generateRandomUsername();
        String password = DataFakeHelper.generateRandomPassword();

        authClient.register(username, password);

        var userFromKafka = usersKafkaClient.getRegisteredUser(username);
        assertThat(userFromKafka.username()).isEqualTo(username);
    }
}
