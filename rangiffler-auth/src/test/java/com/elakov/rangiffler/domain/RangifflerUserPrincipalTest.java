package com.elakov.rangiffler.domain;

import com.elakov.rangiffler.data.Authority;
import com.elakov.rangiffler.data.AuthorityEntity;
import com.elakov.rangiffler.data.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RangifflerUserPrincipalTest {

    private static UserEntity user(boolean enabled, boolean nonExpired, boolean nonLocked, boolean credNonExpired) {
        UserEntity entity = new UserEntity();
        entity.setUsername("bob");
        entity.setPassword("{noop}secret");
        entity.setEnabled(enabled);
        entity.setAccountNonExpired(nonExpired);
        entity.setAccountNonLocked(nonLocked);
        entity.setCredentialsNonExpired(credNonExpired);
        AuthorityEntity read = new AuthorityEntity();
        read.setAuthority(Authority.read);
        AuthorityEntity write = new AuthorityEntity();
        write.setAuthority(Authority.write);
        entity.addAuthorities(read, write);
        return entity;
    }

    @Test
    @DisplayName("delegates credentials and flags to the wrapped entity")
    void delegatesToEntity() {
        RangifflerUserPrincipal principal = new RangifflerUserPrincipal(user(true, true, true, true));

        assertThat(principal.getUsername()).isEqualTo("bob");
        assertThat(principal.getPassword()).isEqualTo("{noop}secret");
        assertThat(principal.isEnabled()).isTrue();
        assertThat(principal.isAccountNonExpired()).isTrue();
        assertThat(principal.isAccountNonLocked()).isTrue();
        assertThat(principal.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @DisplayName("maps each entity authority to a SimpleGrantedAuthority by enum name")
    void mapsAuthorities() {
        RangifflerUserPrincipal principal = new RangifflerUserPrincipal(user(true, true, true, true));

        assertThat(principal.getAuthorities())
                .extracting("authority")
                .containsExactlyInAnyOrder("read", "write");
    }

    @Test
    @DisplayName("reflects disabled / locked flags from the entity")
    void reflectsNegativeFlags() {
        RangifflerUserPrincipal principal = new RangifflerUserPrincipal(user(false, false, false, false));

        assertThat(principal.isEnabled()).isFalse();
        assertThat(principal.isAccountNonExpired()).isFalse();
        assertThat(principal.isAccountNonLocked()).isFalse();
        assertThat(principal.isCredentialsNonExpired()).isFalse();
    }
}
