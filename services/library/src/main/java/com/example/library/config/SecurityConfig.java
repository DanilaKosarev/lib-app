package com.example.library.config;

import com.example.library.security.JWTFilter;
import com.example.library.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    private final JWTFilter jwtFilter;

    private final String[] whiteListUrls;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, JWTFilter jwtFilter, String[] whiteListUrls) {
        this.personDetailsService = personDetailsService;
        this.jwtFilter = jwtFilter;
        this.whiteListUrls = whiteListUrls;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder());

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(whiteListUrls).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.PATCH, "/api/people/**/promote")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.PATCH, "/api/people/**/demote")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.DELETE,"/api/people/**")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.POST,"/api/books")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.PATCH,"/api/books/**/claim")).hasAnyRole("USER","ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.PATCH,"/api/books/**/release")).hasAnyRole("USER","ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.PATCH,"/api/books/**")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.DELETE,"/api/books/**")).hasRole("ADMIN")
                        .anyRequest().hasAnyRole("USER", "ADMIN")
                )
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
