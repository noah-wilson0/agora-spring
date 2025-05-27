package com.agora.debate.member.controller;

import com.agora.debate.member.dto.signin.SignInDto;
import com.agora.debate.security.dto.JwtToken;
import com.agora.debate.member.service.MemberService;
import com.agora.debate.member.valid.ValidationGroups;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO : httponly로 변경
 * 리프레쉬토큰은 쿠기에 저장 : "refreshToken"= refreshToken
 * accesstoken은 httponly로 저장
 */
@RestController
@RequestMapping("/members/sign-in")
@RequiredArgsConstructor
public class MemberSignInController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> signIn(@RequestBody @Validated(ValidationGroups.LoginGroup.class) SignInDto signInDto) {
        JwtToken jwtToken = memberService.signIn(signInDto);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", jwtToken.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(86400)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(jwtToken);
    }

}
