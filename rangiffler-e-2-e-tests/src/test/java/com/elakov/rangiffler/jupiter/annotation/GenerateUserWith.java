package com.elakov.rangiffler.jupiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface GenerateUserWith {

    ClientDB clientDB() default ClientDB.HIBERNATE;

    enum ClientDB {
        HIBERNATE
    }

    String username() default "";

    String password() default "";

    boolean enabled() default true;

    boolean accountNonExpired() default true;

    boolean accountNonLocked() default true;

    boolean credentialsNonExpired() default true;

    boolean deleteUserAfterEach() default true;
}
