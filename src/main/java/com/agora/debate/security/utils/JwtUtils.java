package com.agora.debate.security.utils;

public class JwtUtils {
    // 헤더에서 Bearer 토큰만 추출하는 정적 메서드
    public static String extractAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
