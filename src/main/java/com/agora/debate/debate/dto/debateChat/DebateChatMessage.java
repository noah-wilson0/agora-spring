package com.agora.debate.debate.dto.debateChat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DebateChatMessage {
    private String sender;
    private String content;
    private String type;
}
