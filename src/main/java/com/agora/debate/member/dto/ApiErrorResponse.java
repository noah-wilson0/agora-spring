package com.agora.debate.member.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorResponse {

    private int code;
    private String message;
    private Map<String, String> result; // 필드별 에러 메시지
}

