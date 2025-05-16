package com.agora.debate.global.handler;

import com.agora.debate.global.exception.member.DuplicateMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.agora.debate.member")
public class MemberExceptionHandler {

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<?> handleDuplicate(DuplicateMemberException e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()))
                .body(e.getMessage());
    }
}
