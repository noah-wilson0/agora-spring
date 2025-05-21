package com.agora.debate.global.exception.member;

public class UserNameNotMatchException extends RuntimeException{
    public UserNameNotMatchException() {
        super("로그인 아이디가 일치하지 않습니다.");
    }
}
