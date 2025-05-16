package com.agora.debate.global.handler;

import com.agora.debate.global.exception.DataNotFoundException;
import com.agora.debate.global.handler.response.GlobalErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Order(100)
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<GlobalErrorResponse> handleDataNotFound(DataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new GlobalErrorResponse("DATA_NOT_FOUND", ex.getMessage()));
    }

}
