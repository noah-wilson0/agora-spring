package com.agora.debate.member.handler;

import com.agora.debate.member.exception.DuplicateMemberException;
import com.agora.debate.member.exception.UserNameNotMatchException;
import com.agora.debate.security.excetion.InvalidJwtTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.agora.debate.member")
public class MemberExceptionHandler {

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<?> handleDuplicate(DuplicateMemberException e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()))
                .body(e.getMessage());
    }

    @ExceptionHandler(UserNameNotMatchException.class)
    public ResponseEntity<?> handleUserNameNotMatch(UserNameNotMatchException e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()))
                .body(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .body(e.getMessage());
    }

    @ExceptionHandler(value = InvalidJwtTokenException.class)
    public ResponseEntity<String> handleInvalidJwtTokenException(InvalidJwtTokenException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
