package com.agora.debate.member.utils;

import com.agora.debate.member.dto.signup.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MemberPolicyValidator {

    private final MemberSignUpPolicyChecker memberSignUpPolicyChecker;

    public  void validateSignUpDto(SignUpDto signUpDto) {
        memberSignUpPolicyChecker.validateDuplicateName(signUpDto.getName());
        memberSignUpPolicyChecker.validateDuplicateLoginId(signUpDto.getUsername());
        memberSignUpPolicyChecker.validateDuplicateEmail(signUpDto.getEmail());

    }


}
