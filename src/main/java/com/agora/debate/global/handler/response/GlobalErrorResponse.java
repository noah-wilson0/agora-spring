package com.agora.debate.global.handler.response;

public class GlobalErrorResponse {
    private String error;
    private String message;

    public GlobalErrorResponse(String dataNotFound, String message) {
        this.error = dataNotFound;
        this.message = message;
    }
}
