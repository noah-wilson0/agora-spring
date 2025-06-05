package com.agora.debate.member.controller;


import com.agora.debate.member.dto.ApiResponse;
import com.agora.debate.member.dto.signup.CheckEmailDto;
import com.agora.debate.member.dto.signup.CheckNameDto;
import com.agora.debate.member.dto.signup.CheckUsernameDto;
import com.agora.debate.member.dto.signup.SignUpDto;
import com.agora.debate.member.service.MemberService;
import com.agora.debate.member.valid.ValidationGroups;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * TODO : Swagger-UI 적용시켜보기
 */
@Slf4j
@RestController
@RequestMapping("/members/signup")
@RequiredArgsConstructor
public class MemberSignUpController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody @Validated(ValidationGroups.SignUpGroup.class) SignUpDto signUpDto) {
        memberService.register(signUpDto);

        // 성공 시
        ApiResponse<SignUpDto> successResponse = ApiResponse.<SignUpDto>builder()
                .code(HttpStatus.CREATED.value())
                .message("회원가입 성공")
                .data(signUpDto)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/check-name")
    public ResponseEntity<?> checkName(@RequestBody @Validated(ValidationGroups.CheckNameGroup.class) CheckNameDto checkNameDto) {
        memberService.duplicateName(checkNameDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-id")
    public ResponseEntity<?> checkUsername(@RequestBody @Validated(ValidationGroups.CheckUsernameGroup.class) CheckUsernameDto checkUsernameDto) {
        memberService.duplicateUserName(checkUsernameDto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestBody @Validated(ValidationGroups.CheckEmailGroup.class) CheckEmailDto checkEmailDto) {
        memberService.duplicateEmail(checkEmailDto);
        return ResponseEntity.ok().build();
    }






}
