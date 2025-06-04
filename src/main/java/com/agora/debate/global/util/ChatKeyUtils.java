package com.agora.debate.global.util;

public class ChatKeyUtils {
    public static String buildChannel(Long roomId, String chatType) {
        return String.format("topic-%s-%s", roomId.toString(), chatType);
    }
}
