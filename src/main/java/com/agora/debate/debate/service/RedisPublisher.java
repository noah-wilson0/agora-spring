package com.agora.debate.debate.service;

import com.agora.debate.debate.entity.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void publish(String channel, ChatMessage message) {
        try {
            // Jackson을 사용하여 객체를 JSON 문자열로 직렬화
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.convertAndSend(channel, json);
        } catch (Exception e) {
            throw new RuntimeException("채팅 메시지 직렬화 실패", e);
        }
    }
}
