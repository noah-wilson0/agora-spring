package com.agora.debate.member.dto.naver;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NaverUserInfo {
    private Response response;

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String nickname;

    }
}

