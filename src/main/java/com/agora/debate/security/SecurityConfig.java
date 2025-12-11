package com.agora.debate.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/api/debates/**",       // 토론 요약 기능
                        "/api/chat/history",     // 채팅 기록
                        "/ws-chat/**",           // 웹소켓
                        "/css/**", "/js/**", "/images/**", "/favicon.ico" // 정적 리소스
                );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // 1. CORS 설정 연결 (메서드 호출)
                .cors(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 2. Preflight 요청(OPTIONS)은 무조건 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/debates/**", "/api/chat/history").permitAll()
                        // 4. 나머지 허용 경로들
                        .requestMatchers(
                                "/members/login", "/members/sign-in", "/members/signup",
                                "/members/signup/check-id", "/members/signup/check-name", "/members/signup/check-email",
                                "/oauth/naver", "/oauth/kakao",
                                "/ws-chat/**"
                        ).permitAll()

                        // 5. 회원 전용 경로
                        .requestMatchers(
                                "/members/me", "/auth/refresh", "/auth/me",
                                "/members/update/check-password", "/members/update/change-password",
                                "/members/logout", "/members/update/change-info",
                                "/api/boards"
                        ).hasRole("USER")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.simpSubscribeDestMatchers("/sub/**").permitAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//
//        // 🔥 [핵심 해결책] 🔥
//        // setAllowedOrigins("*") 대신 setAllowedOriginPatterns("*")를 써야 에러가 안 납니다.
//        // 모든 주소(3000, 5173 등) 다 허용됩니다.
//        config.setAllowedOriginPatterns(List.of("*"));
//
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true); // 자격 증명 허용
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
}