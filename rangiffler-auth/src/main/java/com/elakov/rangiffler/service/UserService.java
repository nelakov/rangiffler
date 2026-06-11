package com.elakov.rangiffler.service;

import com.elakov.rangiffler.config.KafkaProducerConfig;
import com.elakov.rangiffler.data.Authority;
import com.elakov.rangiffler.data.AuthorityEntity;
import com.elakov.rangiffler.data.UserEntity;
import com.elakov.rangiffler.data.repository.UserRepository;
import com.elakov.rangiffler.model.UserJson;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, UserJson> kafkaTemplate;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       KafkaTemplate<String, UserJson> kafkaTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaTemplate = kafkaTemplate;
    }

    public @Nonnull
    String registerUser(@Nonnull String username, @Nonnull String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEnabled(true);
        userEntity.setAccountNonExpired(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(password));

        AuthorityEntity readAuthorityEntity = new AuthorityEntity();
        readAuthorityEntity.setAuthority(Authority.read);
        readAuthorityEntity.setUser(userEntity);
        AuthorityEntity writeAuthorityEntity = new AuthorityEntity();
        writeAuthorityEntity.setAuthority(Authority.write);
        writeAuthorityEntity.setUser(userEntity);

        userEntity.addAuthorities(readAuthorityEntity, writeAuthorityEntity);
        String savedUsername = userRepository.save(userEntity).getUsername();

        kafkaTemplate.send(KafkaProducerConfig.USERS_TOPIC, new UserJson(savedUsername))
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        LOG.error("### Kafka topic [{}] failed to send message for user: {}",
                                KafkaProducerConfig.USERS_TOPIC, savedUsername, ex);
                    } else {
                        LOG.info("### Kafka topic [{}] sent message for user: {} (offset={})",
                                KafkaProducerConfig.USERS_TOPIC, savedUsername,
                                result.getRecordMetadata().offset());
                    }
                });

        return savedUsername;
    }
}