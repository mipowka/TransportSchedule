package org.example.transportschedule.security;

import lombok.RequiredArgsConstructor;
import org.example.transportschedule.model.entity.User;
import org.example.transportschedule.repository.user.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final static String ROLE_PREFIX = "ROLE_";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        List<GrantedAuthority> roles = new ArrayList<>();

        String role = ROLE_PREFIX + user.getRole();
        roles.add(new SimpleGrantedAuthority(role));

        return new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                roles
        );
    }
}
