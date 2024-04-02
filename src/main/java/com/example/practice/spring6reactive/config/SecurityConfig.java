package com.example.practice.spring6reactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(authorizeExchange -> authorizeExchange.anyExchange().authenticated())
            .oauth2ResourceServer(oAuth2ResourceServer -> oAuth2ResourceServer.jwt(Customizer.withDefaults()))
            .build();
    }
}
