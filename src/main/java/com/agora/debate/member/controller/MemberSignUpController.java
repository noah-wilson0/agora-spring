package com.agora.debate.member.controller;


import com.agora.debate.member.dto.ApiResponse;
import com.agora.debate.member.dto.ApiErrorResponse;
import com.agora.debate.member.dto.signup.SignUpDto;
import com.agora.debate.member.entity.Member;
import com.agora.debate.member.service.MemberService;
import com.agora.debate.member.valid.ValidationGroups;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO : Swagger-UI 적용시켜보기
 *
 *
 */
@Slf4j
@RestController
@RequestMapping("/member/signup")
@RequiredArgsConstructor
public class MemberSignUpController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody @Validated(ValidationGroups.SignUpGroup.class) SignUpDto signUpDto, Errors errors) {

        if (errors.hasErrors()) {
            Map<String, String> fieldErrors = errors.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage(),
                            (msg1, msg2) -> msg1 + "; " + msg2
                    ));

            ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("형식이 잘못되었습니다.")
                    .result(fieldErrors)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
        }
        Member member = memberService.register(signUpDto);


        // 성공 시
        ApiResponse<SignUpDto> successResponse = ApiResponse.<SignUpDto>builder()
                .code(HttpStatus.CREATED.value())
                .message("회원가입 성공")
                .data(signUpDto)
                .build();

        return ResponseEntity.ok(successResponse);
    }




}
