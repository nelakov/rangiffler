package com.elakov.rangiffler.jupiter.annotation.creation;


import com.elakov.rangiffler.jupiter.callback.creation.CreateUserInAuthDatabaseCallback;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.PARAMETER})
@ExtendWith(CreateUserInAuthDatabaseCallback.class)
public @interface CreateUserInDB {

    boolean handleAnnotation() default true;

    String username() default "";

    String password() default "";

    boolean enabled() default true;

    boolean accountNonExpired() default true;

    boolean accountNonLocked() default true;

    boolean credentialsNonExpired() default true;

    boolean deleteUserAfterEach() default true;

}
