package com.agora.debate.member.controller;

import com.agora.debate.member.dto.ApiErrorResponse;
import com.agora.debate.member.dto.signin.SignInDto;
import com.agora.debate.member.security.dto.JwtToken;
import com.agora.debate.member.service.MemberService;
import com.agora.debate.member.valid.ValidationGroups;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO : 로그인 구현
 *  로그인시 jwt(http only cookie사용, 추후 secure cookie 사용해보기) 발급 시큐어 인증 후 인가되는 구조로 "추후" 변경
 */
@Slf4j
@RestController
@RequestMapping("/members/sign-in")
@RequiredArgsConstructor
public class MemberSignInController {
    private final MemberService memberService;


    @PostMapping
    public ResponseEntity<?> signIn(@RequestBody @Validated(ValidationGroups.LoginGroup.class) SignInDto signInDto, Errors errors) {
        if (errors.hasErrors()) {
            log.info("signIn error");
            Map<String, String> fieldErrors = errors.getFieldErrors().stream().
                    collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage(),
                            (msg1, msg2) -> msg1 + ", " + msg2
                    ));
            ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("형식이 잘못되었습니다.")
                    .result(fieldErrors)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
        }
        JwtToken jwtToken = memberService.signIn(signInDto);
        log.info("request username = {}, password = {}", signInDto.getUsername(), signInDto.getPassword());
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }

}
