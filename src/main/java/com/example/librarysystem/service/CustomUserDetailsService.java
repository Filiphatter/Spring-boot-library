package com.example.librarysystem.service;

import com.example.librarysystem.entity.User;
import com.example.librarysystem.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("försöker att authentikera: " + email); //debuggin

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("User not found: " + email); //debugg
                    return new UsernameNotFoundException("Ingen användare med e-post: " + email);
                });

        System.out.println("Found user: " + user.getEmail());
        System.out.println("User has " + user.getRoles().size() + " roles:"); //debugg

        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> {
                    String authority = "ROLE_" + role.getName();
                    System.out.println("   - Adding authority: " + authority); //debugg
                    return new SimpleGrantedAuthority(authority);
                })
                .collect(Collectors.toSet());

        System.out.println("Authorities: " + authorities); //debugg

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}