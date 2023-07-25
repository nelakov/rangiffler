package com.elakov.rangifflerauth.service;

import com.elakov.rangifflerauth.domain.RangifflerUserPrincipal;
import com.elakov.rangifflerauth.data.UserEntity;
import com.elakov.rangifflerauth.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class RangifflerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public RangifflerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new RangifflerUserPrincipal(user);
    }
}
