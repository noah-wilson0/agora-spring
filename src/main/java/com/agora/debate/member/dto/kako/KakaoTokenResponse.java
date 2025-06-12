package com.agora.debate.member.dto.kako;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoTokenResponse {
    private String token_type;
    private String access_token;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;
}