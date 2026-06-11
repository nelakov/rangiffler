package com.elakov.rangiffler.service;

import com.elakov.rangiffler.model.RegistrationModel;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class EqualPasswordsValidatorTest {

    private final EqualPasswordsValidator validator = new EqualPasswordsValidator();

    private static RegistrationModel form(String password, String passwordSubmit) {
        RegistrationModel model = new RegistrationModel();
        model.setUsername("bob");
        model.setPassword(password);
        model.setPasswordSubmit(passwordSubmit);
        return model;
    }

    @Test
    @DisplayName("matching passwords are valid and no violation is raised")
    void matchingPasswordsAreValid() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class, RETURNS_DEEP_STUBS);

        boolean result = validator.isValid(form("secret", "secret"), context);

        assertThat(result).isTrue();
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    @DisplayName("mismatched passwords are invalid and a violation on 'password' is built")
    void mismatchedPasswordsAreInvalid() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class, RETURNS_DEEP_STUBS);

        boolean result = validator.isValid(form("secret", "different"), context);

        assertThat(result).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate());
    }
}
