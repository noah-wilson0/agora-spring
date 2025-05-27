package com.agora.debate.member.service;

import com.agora.debate.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {
    private final RedisTemplate redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    public void addBlacklistToken(String token, String type) {
        long atExpiration = jwtTokenProvider.getExpiryTime(token); // 만료시간(ms)
        long now = System.currentTimeMillis();
        long atTtl = atExpiration - now;
        if (atTtl > 0) { // 만료된 토큰이 아닐 때만
            redisTemplate.opsForValue().set(
                    "blacklist:" + token, type, atTtl, TimeUnit.MILLISECONDS
            );
        }
    }
}
