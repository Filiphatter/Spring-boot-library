package com.example.librarysystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        //Publika endpoints
                        .requestMatchers("/register", "/login").permitAll()
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/books", "/books/search").permitAll()
                        .requestMatchers("/test/**").permitAll() //ta bort senare
                        //.requestMatchers("/users/migrate-passwords").permitAll() för att migrera lösenord temporär lösning

                        //Kräver USER eller ADMIN
                        .requestMatchers("/loans/add").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/loans/{loanId}/return").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/loans/{loanId}/extend").hasAnyRole("USER", "ADMIN")

                        //Endast ADMIN
                        .requestMatchers("/books/add").hasRole("ADMIN")
                        .requestMatchers("/authors/add").hasRole("ADMIN")
                        .requestMatchers("/loans/all").hasRole("ADMIN")
                        .requestMatchers("/users/**").hasRole("ADMIN")

                        //Allt annat kräver inloggning
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()) //för postman basic auth enligt ai
                .formLogin(form -> form
                        .permitAll() //Spring Securitys standard login form
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean //password encoder
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


//anna.andersson@email.com password123 user
//Alice@example.com secure123 admin