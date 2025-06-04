package com.agora.debate.debate.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessage {
    private Long roomId;
    private String chatType;
    private String sender;
    private String message;
    private LocalDateTime sentAt;

    public String getChanel() { return "topic-" + roomId.toString() + "-" + chatType; }
    public String getKey() { return "chat:room:" + getChanel(); }
}
