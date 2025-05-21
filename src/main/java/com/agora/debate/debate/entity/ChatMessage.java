package com.agora.debate.debate.entity;

import lombok.*;

@Getter
@Setter
public class ChatMessage {
    private String roomId;
    private String sender;
    private String message;
}