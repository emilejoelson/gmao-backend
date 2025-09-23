package com.project.gmao.core.security;

import lombok.RequiredArgsConstructor;

import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.gmao.features.authentication.entity.User;
import com.project.gmao.features.authentication.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        // Force initialization of lazy collections
        Hibernate.initialize(user.getRoles());
        user.getRoles().forEach(role -> {
            Hibernate.initialize(role.getPermissions());
        });
        
        return UserPrincipal.create(user);
    }
}
