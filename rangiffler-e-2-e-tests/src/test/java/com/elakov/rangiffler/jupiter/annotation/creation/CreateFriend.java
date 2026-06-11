package com.elakov.rangiffler.jupiter.annotation.creation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
public @interface CreateFriend {

    String username() default "";

    String firstname() default "";

    String lastname() default "";

    CreatePhoto[] photos() default {};

}
