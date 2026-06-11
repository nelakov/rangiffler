package com.elakov.rangiffler.service;

import com.elakov.rangiffler.data.UserEntity;
import com.elakov.rangiffler.data.repository.FakeUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Chicago-school / EO (fake) variant — same behaviour as
 * {@link RangifflerUserDetailsServiceTest} but driven by a real in-memory
 * {@link FakeUserRepository} instead of a Mockito stub. No when()/verify();
 * the test reads as a plain object collaboration.
 */
class RangifflerUserDetailsServiceWithFakeObjectsTest {

    private static UserEntity user(String username) {
        UserEntity entity = new UserEntity();
        entity.setUsername(username);
        entity.setPassword("{noop}secret");
        return entity;
    }

    @Test
    @DisplayName("returns principal wrapping the user found by username")
    void loadsExistingUser() {
        var service = new RangifflerUserDetailsService(new FakeUserRepository(user("bob")));

        UserDetails details = service.loadUserByUsername("bob");

        assertThat(details.getUsername()).isEqualTo("bob");
        assertThat(details.getPassword()).isEqualTo("{noop}secret");
    }

    @Test
    @DisplayName("throws UsernameNotFoundException when the user is absent")
    void throwsWhenUserMissing() {
        var service = new RangifflerUserDetailsService(new FakeUserRepository());

        assertThatThrownBy(() -> service.loadUserByUsername("ghost"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("ghost");
    }
}
