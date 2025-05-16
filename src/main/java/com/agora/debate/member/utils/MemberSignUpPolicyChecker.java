package com.agora.debate.member.utils;

import com.agora.debate.global.exception.member.DuplicateMemberException;
import com.agora.debate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MemberSignUpPolicyChecker {
    private final MemberRepository memberRepository;

    public boolean validateDuplicateName(String name) {
        if (memberRepository.existsByName(name)) {
            throw new DuplicateMemberException("이미 존재하는 닉네임입니다.");
        }
        return true;
    }

    public boolean validateDuplicateLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new DuplicateMemberException("이미 존재하는 아이디입니다.");
        }
        return true;
    }

    public boolean validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateMemberException("이미 존재하는 이메일입니다.");
        }
        return true;
    }

}
