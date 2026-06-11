package com.elakov.rangiffler.service;

import com.elakov.rangiffler.config.KafkaProducerConfig;
import com.elakov.rangiffler.data.Authority;
import com.elakov.rangiffler.data.UserEntity;
import com.elakov.rangiffler.data.repository.UserRepository;
import com.elakov.rangiffler.model.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private KafkaTemplate<String, UserJson> kafkaTemplate;

    @Captor
    private ArgumentCaptor<UserEntity> userCaptor;
    @Captor
    private ArgumentCaptor<UserJson> kafkaPayloadCaptor;

    private UserService userService;

    private UserService newService() {
        return new UserService(userRepository, passwordEncoder, kafkaTemplate);
    }

    @Test
    @DisplayName("encodes password, persists an enabled user with read+write authorities, returns saved username")
    void registersUser() {
        userService = newService();
        when(passwordEncoder.encode("secret")).thenReturn("hashed");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(kafkaTemplate.send(eq(KafkaProducerConfig.USERS_TOPIC), any(UserJson.class)))
                .thenReturn(new CompletableFuture<>());

        String result = userService.registerUser("bob", "secret");

        assertThat(result).isEqualTo("bob");
        org.mockito.Mockito.verify(userRepository).save(userCaptor.capture());
        UserEntity saved = userCaptor.getValue();
        assertThat(saved.getUsername()).isEqualTo("bob");
        assertThat(saved.getPassword()).isEqualTo("hashed");
        assertThat(saved.getEnabled()).isTrue();
        assertThat(saved.getAccountNonExpired()).isTrue();
        assertThat(saved.getAccountNonLocked()).isTrue();
        assertThat(saved.getCredentialsNonExpired()).isTrue();
        assertThat(saved.getAuthorities())
                .extracting("authority")
                .containsExactlyInAnyOrder(Authority.read, Authority.write);
    }

    @Test
    @DisplayName("publishes a UserJson for the saved username to the users topic")
    void publishesKafkaEvent() {
        userService = newService();
        when(passwordEncoder.encode(any())).thenReturn("hashed");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(kafkaTemplate.send(eq(KafkaProducerConfig.USERS_TOPIC), kafkaPayloadCaptor.capture()))
                .thenReturn(new CompletableFuture<>());

        userService.registerUser("bob", "secret");

        assertThat(kafkaPayloadCaptor.getValue().username()).isEqualTo("bob");
    }
}
