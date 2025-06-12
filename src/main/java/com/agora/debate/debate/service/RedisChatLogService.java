package com.agora.debate.debate.service;

import com.agora.debate.debate.entity.ChatMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisChatLogService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    // 저장
    public void saveMessage(String roomId, ChatMessage message) {
        try {
            String key = "chat:room:" + roomId;
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.opsForList().rightPush(key, json);
            redisTemplate.opsForList().trim(key, -100, -1); // 최근 100개만 유지
        } catch (Exception e) {
            throw new RuntimeException("채팅 메시지 저장 실패", e);
        }
    }

    // 조회
    public List<ChatMessage> getRecentMessages(String roomId, int count) {
        String key = "chat:room:" + roomId;
        List<String> rawList = redisTemplate.opsForList().range(key, -count, -1);
        if (rawList == null) return List.of();
        return rawList.stream().map(json -> {
            try {
                return objectMapper.readValue(json, ChatMessage.class);
            } catch (Exception e) {
                return null;
            }
        }).filter(msg -> msg != null).collect(Collectors.toList());
    }
}

