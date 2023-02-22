package com.webapp.webapp.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;

// import com.webapp.webapp.service.UserService;
// import com.webapp.webapp.dto.*;

// @Configuration
// @EnableWebSecurity
// public class AuthenticateUser {

// @Bean
// public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

// http.csrf()
// .disable()
// .authorizeRequests()
// .antMatchers("/v1/account/*")
// .fullyAuthenticated()
// .anyRequest()
// .permitAll()
// .and()
// .httpBasic();
// return http.build();
// }

// }
