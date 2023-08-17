package com.elakov.rangiffler.data.repository.authDAO;

import com.elakov.rangiffler.data.entity.AuthUserEntity;
import com.elakov.rangiffler.data.entity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

public interface AuthRepository {

    PasswordEncoder encoder = createDelegatingPasswordEncoder();

    int createUser(AuthUserEntity user);

    UserEntity getUser(AuthUserEntity user);

    String findUserById(String userName);

    int removeUser(AuthUserEntity user);

}
