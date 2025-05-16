package com.agora.debate.member.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private int code;       // HTTP 상태 코드
    private String message;   // 설명 메시지
    private T data;           // 실질적인 응답 데이터
}
