package com.agora.debate.global.exception.member;

import com.agora.debate.global.exception.DataNotFoundException;

public class UserPasswordNotMatchException extends DataNotFoundException {
    public UserPasswordNotMatchException(String loginId) {
        super("로그인 ID [" + loginId + "]의 비밀번호가 일치하지 않습니다.");
    }
}