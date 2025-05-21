package com.agora.debate.debate.service;

import com.agora.debate.debate.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;


@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
    private final SimpMessagingTemplate messagingTemplate;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody());
        ChatMessage chatMessage = new Gson().fromJson(msg, ChatMessage.class);
        messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
    }
}
