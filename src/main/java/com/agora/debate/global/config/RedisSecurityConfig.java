package com.agora.debate.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
public class RedisSecurityConfig {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (WebSocket엔 불필요)
//                .authorizeHt  tpRequests(authz -> authz
//                        .requestMatchers("/ws-chat/**").permitAll() // WebSocket 경로 전체 허용
//                        .anyRequest().permitAll() // 나머지도 모두 허용 (테스트용, 실제 서비스시에는 제한)
//                );
//        return http.build();
//    }
}