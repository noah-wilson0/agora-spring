package com.agora.debate.member.controller.naver;

import com.agora.debate.member.dto.naver.NaverReqeustDto;
import com.agora.debate.member.service.naver.NaverAuthService;
import com.agora.debate.member.service.naver.NaverService;
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
public class NaverOAuthController {
    private final NaverAuthService naverAuthService;
    private final NaverService naverService;

    @PostMapping("/naver")
    public ResponseEntity<?> signInNaver(@RequestBody NaverReqeustDto reqeustDTO) {
        log.info("code={},state={}", reqeustDTO.getCode(), reqeustDTO.getState());
        String accessToken = naverAuthService.getAccessToken(reqeustDTO.getCode(), reqeustDTO.getState());
        log.info("naverAccessToken={}", accessToken);
        Map<String, Object> userInfo = null;
        try {
            userInfo = naverAuthService.getUserInfo(accessToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JwtToken jwtToken = null;
        try {
            jwtToken = naverService.signIn(userInfo);
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
