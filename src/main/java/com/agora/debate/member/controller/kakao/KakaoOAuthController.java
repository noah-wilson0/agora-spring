package com.agora.debate.member.controller.kakao;

import com.agora.debate.member.dto.kako.KakaoRequestDto;
import com.agora.debate.member.service.kakao.KakaoService;
import com.agora.debate.member.service.kakao.KaokaoAuthService;
import com.agora.debate.security.dto.JwtToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class KakaoOAuthController {
    private final KaokaoAuthService kaokaoAuthService;
    private final KakaoService kakaoService;

    @PostMapping("/kakao")
    public ResponseEntity<?> signInKakao(@RequestBody KakaoRequestDto kakaoRequestDto) {
        log.info("code={}",kakaoRequestDto.getCode());
        String accessToken = kaokaoAuthService.getAccessToken(kakaoRequestDto.getCode());
        Map<String, Object> userInfo=null;

        log.info("유저 정보 찾기");
        try {
           userInfo = kaokaoAuthService.getUserInfo(accessToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("userInfo:{}",userInfo.toString());

        JwtToken jwtToken = null;
        try {
            jwtToken = kakaoService.signIn(userInfo);
        } catch (BadCredentialsException e) {
            log.info("로그인 실패");
            throw new RuntimeException(e);
        }
        log.info("DB 등록 완료 ");
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
