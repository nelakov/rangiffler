package com.elakov.rangiffler.jupiter.annotation.test;

import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.jupiter.callback.creation.ApiLoginCallback;
import com.elakov.rangiffler.jupiter.callback.creation.CreateUserCallback;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO: АПИ ЛОГИН как в Ниффлере
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({ApiLoginCallback.class, CreateUserCallback.class})
public @interface ApiLoginTest {

    CreateUser[] user() default {};

    String username() default "";

    String password() default "";
}
