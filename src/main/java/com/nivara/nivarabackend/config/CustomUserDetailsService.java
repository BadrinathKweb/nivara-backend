package com.nivara.nivarabackend.config;

import com.nivara.nivarabackend.entity.User;
import com.nivara.nivarabackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            Long userId = Long.parseLong(username);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return org.springframework.security.core.userdetails.User
                    .withUsername(String.valueOf(user.getId()))
                    .password("")
                    .authorities("USER")
                    .build();

        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Invalid user id");
        }
    }
}

