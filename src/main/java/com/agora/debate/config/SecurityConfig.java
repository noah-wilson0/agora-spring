package com.agora.debate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 인증 없이 모두 허용
                )
                .csrf(csrf -> csrf.disable())          // CSRF 비활성화 (람다 방식)
                .httpBasic(httpBasic -> httpBasic.disable()) // 로그인 팝업 비활성화
                .build();
    }
}
