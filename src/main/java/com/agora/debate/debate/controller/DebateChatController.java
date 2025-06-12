package com.agora.debate.debate.controller;

import com.agora.debate.debate.dto.debateChat.DebateChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DebateChatController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public DebateChatMessage sendMessage(@Payload DebateChatMessage message) {
        return message;
    }
}
