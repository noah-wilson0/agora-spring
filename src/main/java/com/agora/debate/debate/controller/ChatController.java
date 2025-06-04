package com.agora.debate.debate.controller;

import com.agora.debate.debate.entity.ChatMessage;
import com.agora.debate.debate.service.RedisPublisher;
import com.agora.debate.debate.service.DebateRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final DebateRoomService redisChatLogService;

    // 실시간 채팅 메시지 발송
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        String chanel = message.getChanel();
        redisPublisher.publish(chanel, message);
        redisChatLogService.saveMessage(message); // 로그 저장
    }

    // 과거 메시지 REST 조회
    @GetMapping("/api/chat/history")
    public List<ChatMessage> getChatHistory(
            @RequestParam Long roomId,
            @RequestParam String chatType,
            @RequestParam(defaultValue = "50") int count) {
        return redisChatLogService.getRecentMessages(roomId, chatType, count);
    }
}

