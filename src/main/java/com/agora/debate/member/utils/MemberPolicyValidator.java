package com.agora.debate.member.utils;

import com.agora.debate.member.dto.signup.CheckEmailDto;
import com.agora.debate.member.dto.signup.CheckNameDto;
import com.agora.debate.member.dto.signup.CheckUsernameDto;
import com.agora.debate.member.dto.signup.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MemberPolicyValidator {

    private final MemberSignUpPolicyChecker memberSignUpPolicyChecker;

    public  void duplicateSignUpDto(SignUpDto signUpDto) {
        memberSignUpPolicyChecker.validateDuplicateName(signUpDto.getName());
        memberSignUpPolicyChecker.validateDuplicateLoginId(signUpDto.getUsername());
        memberSignUpPolicyChecker.validateDuplicateEmail(signUpDto.getEmail());

    }
    public  void duplicateUserName(CheckUsernameDto checkUsernameDto) {
        memberSignUpPolicyChecker.validateDuplicateLoginId(checkUsernameDto.getId());

    }
    public  void duplicateName(CheckNameDto checkNameDto) {
        memberSignUpPolicyChecker.validateDuplicateName(checkNameDto.getName());
    }
    public  void duplicateEmail(CheckEmailDto checkEmailDto) {
        memberSignUpPolicyChecker.validateDuplicateEmail(checkEmailDto.getEmail());
    }


}
