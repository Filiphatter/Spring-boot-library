package com.example.librarysystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                //.csrf(csrf -> csrf
                 //       .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //)
                .csrf(csrf -> csrf.disable())
                /*      .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyTrue())

                        Om jag vill ha på med true eller false eller av helt o hållet. (av helt o hållet just nu för lättare testning.)

                        CSRF skydd, CSRF är når en webbsida "lurar" min browser att skicka request till applikationen när man är inloggad.
                        utan skyddet kan en skapare förslagsvis skapa formurlär som raderar delar i databasen. Genom cookierepository så lagrar man tokens i session,
                        Sedan cookieCSRFTokenRepo gör att man lagrar tokens i cookiesen istället.

                        withHttpOnlyTrue så kan javascript INTE komma åt tokenen försvarar mer mot XSS-attacker. (tror jag)
                        -.-.-false så kan javascript komma åt tokenen men ger mindre säkerhet. Med false i produktion och pushande till live kör man true.
                */

                .authorizeHttpRequests(auth -> auth
                        //Publika endpoints
                        .requestMatchers("/register", "/login").permitAll()
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/books", "/books/search").permitAll()
                        //.requestMatchers("/users/migrate-passwords").permitAll() för att migrera lösenord temp lösning

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

                .httpBasic(Customizer.withDefaults()) //för postman basic auth
                .formLogin(form -> form
                        .permitAll() //Spring Securities standard login form
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