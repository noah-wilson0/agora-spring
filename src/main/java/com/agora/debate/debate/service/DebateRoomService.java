package com.agora.debate.debate.service;

import com.agora.debate.debate.entity.Board;
import com.agora.debate.debate.entity.ChatMessage;
import com.agora.debate.global.util.ChatKeyUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DebateRoomService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final List<String> CHAT_TYPES = List.of("free", "ai", "pros", "cons", "debate");

    // 메시지 저장
    public void saveMessage(ChatMessage message){
        try {
            String key = message.getKey();
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.opsForList().rightPush(key, json);
            redisTemplate.opsForList().trim(key, -100, -1); // 최근 100개만 유지
        } catch (Exception e) {
            throw new RuntimeException("채팅 메시지 저장 실패", e);
        }
    }

    // 조회
    public List<ChatMessage> getRecentMessages(Long roomId, String chatType, int count) {
        String key = "chat:room:" + ChatKeyUtils.buildChannel(roomId, chatType);
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

    // 방 종료(상태 변경) 시 호출
    public void closeRoom(Board board) {
        for (String chatType : CHAT_TYPES) {
            String chatKey = "chat:room" + ChatKeyUtils.buildChannel(board.getBoardId(), chatType);
            redisTemplate.delete(chatKey);
        }
    }
}
