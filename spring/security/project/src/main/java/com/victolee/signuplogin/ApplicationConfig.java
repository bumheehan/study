package com.victolee.signuplogin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ApplicationConfig {

    public static ObjectMapper ObjectMapper;

    @Bean
    public void getObjectMapper() {
	ObjectMapper objectMapper = new ObjectMapper();
	ApplicationConfig.ObjectMapper = objectMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
    }

}
