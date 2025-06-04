package com.agora.debate.member.controller;

import com.agora.debate.member.dto.signin.SignInDto;
import com.agora.debate.security.dto.JwtToken;
import com.agora.debate.member.service.MemberService;
import com.agora.debate.member.valid.ValidationGroups;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/members/sign-in")
@RequiredArgsConstructor
public class MemberSignInController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> signIn(@RequestBody @Validated(ValidationGroups.SignInGroup.class) SignInDto signInDto) {
        log.info("토큰 생성 시작");
        log.info(signInDto.getUsername());
        log.info(signInDto.getPassword());
        JwtToken jwtToken = memberService.signIn(signInDto);
        log.info("토큰 생성완료");
        ResponseCookie atCookie = ResponseCookie.from("accessToken", jwtToken.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(3600)
                .sameSite("Strict")
                .build();

        ResponseCookie rtCookie = ResponseCookie.from("refreshToken", jwtToken.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(86400)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,atCookie.toString())
                .header(HttpHeaders.SET_COOKIE,rtCookie.toString())
                .body(jwtToken);
    }

}
