package com.elakov.rangiffler.data.repository.auth;

import com.elakov.rangiffler.data.entity.auth.UserAuthEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

public interface AuthRepository {

    PasswordEncoder PASSWORD_ENCODER = createDelegatingPasswordEncoder();

    int createUser(UserAuthEntity user);

    UserAuthEntity getUser(UserAuthEntity user);

    String findUserById(String userName);

    int removeUser(UserAuthEntity user);

}
