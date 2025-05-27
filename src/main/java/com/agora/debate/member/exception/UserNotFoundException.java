package com.agora.debate.member.exception;

import com.agora.debate.global.exception.DataNotFoundException;

public class UserNotFoundException extends DataNotFoundException {
    public UserNotFoundException(String username) {
        super("ID " + username + "에 해당하는 사용자가 없습니다.");
    }
}
