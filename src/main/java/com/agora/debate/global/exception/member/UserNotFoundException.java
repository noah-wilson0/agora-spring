package com.agora.debate.global.exception.member;

import com.agora.debate.global.exception.DataNotFoundException;

public class UserNotFoundException extends DataNotFoundException {
    public UserNotFoundException(Long userId) {
        super("ID " + userId + "에 해당하는 사용자가 없습니다.");
    }
}
