package com.friendbook1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//import com.friendbook1.services.CustomUserDetailsServices;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    UserDetailsService getDetailsService() {
        return new CustomUserDetailsServices(); 
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
    	System.out.println("lllllfndnllll");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(getDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        System.out.println("yyyy"+authProvider.toString());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    	
    	System.out.println("mmmmmm");
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(authenticationProvider()) 
            .build();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(authz -> authz
                .requestMatchers( "/RegisterPage","/userLogin", "/register", "/static/**","/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/")
                .loginProcessingUrl("/login")
               .defaultSuccessUrl("/home", true) 
                .failureUrl("/login?error=true")
                .permitAll());
        return http.build();
    }
}
