package com.agora.debate.debate.service;

import com.agora.debate.debate.entity.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    // Spring이 기본 빈으로 관리하는 ObjectMapper를 사용 (Autowired로 주입)
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            ChatMessage chatMessage = objectMapper.readValue(msg, ChatMessage.class);
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getChanel(), chatMessage);

        } catch (Exception e) {
            e.printStackTrace(); // 혹은 로거로 처리
        }
    }
}
