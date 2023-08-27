package com.elakov.rangiffler.jupiter.annotation.creation;

import com.elakov.rangiffler.jupiter.callback.creation.CreateUserCallback;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, PARAMETER})
@Retention(RUNTIME)
@ExtendWith(CreateUserCallback.class)
public @interface CreateUser {

    String username() default "";

    String password() default "";

    String avatarPath() default "";

    CreatePhoto[] photos() default {};

    CreateFriend[] friends() default {};

    CreateFriend[] incomeInvitations() default {};

    CreateFriend[] outcomeInvitations() default {};

}
