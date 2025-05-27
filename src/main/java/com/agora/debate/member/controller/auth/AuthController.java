package com.agora.debate.member.controller.auth;

import com.agora.debate.security.JwtTokenProvider;
import com.agora.debate.security.JwtUtils;
import com.agora.debate.member.service.CustomUserDetailsService;
import com.agora.debate.member.service.JwtBlacklistService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * TODO:
 *  액세스 토큰 http only적용
 *  AT http only로 덮어쓰기
 */
@Slf4j
@Controller
@RequestMapping("/auth/refresh")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisTemplate redisTemplate;
    private final JwtBlacklistService jwtBlacklistService;


    @PostMapping
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String authorizationHeader,
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("리프레시 토큰이 존재하지 않습니다.");
        }

        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String username = claims.getSubject();
        String storedToken = (String) redisTemplate.opsForValue().get("refresh:" + username);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("리프레시 토큰이 일치하지 않습니다.");
        }

        String accessToken = JwtUtils.extractAccessToken(authorizationHeader);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        jwtBlacklistService.addBlacklistToken(accessToken,"refresh");

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        return ResponseEntity.ok(newAccessToken);

    }
}
