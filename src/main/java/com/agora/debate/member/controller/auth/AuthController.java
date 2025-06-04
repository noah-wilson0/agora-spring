package com.agora.debate.member.controller.auth;

import com.agora.debate.member.entity.Member;
import com.agora.debate.security.JwtTokenProvider;
import com.agora.debate.member.service.CustomUserDetailsService;
import com.agora.debate.member.service.JwtBlacklistService;
import com.agora.debate.security.utils.JwtCookieUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisTemplate redisTemplate;
    private final JwtBlacklistService jwtBlacklistService;


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                    @CookieValue(value = "accessToken",required = false)String accessToken) {

        if (refreshToken == null && accessToken == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("리프레시 토큰이 존재하지 않습니다.");
        }
        log.info("AT재생성 진행");
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String username = claims.getSubject();
        String storedToken = (String) redisTemplate.opsForValue().get("refresh:" + username);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("리프레시 토큰이 일치하지 않습니다.");
        }


        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        jwtBlacklistService.addBlacklistToken(accessToken,"refresh");

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication, 3600000);
        log.info("재생성 완료");
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE,
                        JwtCookieUtils.createAccessTokenCookie(newAccessToken, 3600000).toString()
                )
                .body("새로운 AccessToken이 발급되었습니다.");
    }
    @GetMapping("/me")
    public ResponseEntity<?> checkLoginStatus(@AuthenticationPrincipal Member member) {
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인되지 않은 사용자");
        }
        log.info("로그인한 사용자");
        return ResponseEntity.ok().body(Map.of("name", member.getName()));
    }
}
