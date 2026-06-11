package com.elakov.rangiffler.service;

import com.elakov.rangiffler.data.UserEntity;
import com.elakov.rangiffler.data.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * London-school (mock) variant — the collaborator is a Spring Data interface
 * with a large surface, so a stub is the pragmatic choice.
 * Compare with {@link RangifflerUserDetailsServiceWithFakeObjectsTest}.
 */
@ExtendWith(MockitoExtension.class)
class RangifflerUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RangifflerUserDetailsService userDetailsService;

    @Test
    @DisplayName("returns principal wrapping the user found by username")
    void loadsExistingUser() {
        UserEntity entity = new UserEntity();
        entity.setUsername("bob");
        entity.setPassword("{noop}secret");
        when(userRepository.findByUsername("bob")).thenReturn(entity);

        UserDetails details = userDetailsService.loadUserByUsername("bob");

        assertThat(details.getUsername()).isEqualTo("bob");
        assertThat(details.getPassword()).isEqualTo("{noop}secret");
    }

    @Test
    @DisplayName("throws UsernameNotFoundException when the user is absent")
    void throwsWhenUserMissing() {
        when(userRepository.findByUsername("ghost")).thenReturn(null);

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("ghost"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("ghost");
    }
}
