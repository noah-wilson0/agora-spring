package com.agora.debate.member.controller;

import com.agora.debate.member.dto.signin.SignInDto;
import com.agora.debate.member.service.JwtBlacklistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MemberSignInControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JwtBlacklistService jwtBlacklistService;

    private String accessToken;
    private String refreshToken;
    @AfterEach
    void tearDown() {
        if (refreshToken != null) {
            jwtBlacklistService.addBlacklistToken(refreshToken, "test");
        }
        if (accessToken != null) {
            jwtBlacklistService.addBlacklistToken(accessToken, "test");
        }
    }
    @DisplayName("회원가입_성공_테스트")
    @Test
    void signIn() throws Exception {

        String loginJson = objectMapper.writeValueAsString(new SignInDto("test1234", "test12345678@"));
        MvcResult result = mockMvc.perform(post("/members/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andReturn();
        accessToken = result.getResponse().getCookie("accessToken").getValue();
        refreshToken = result.getResponse().getCookie("refreshToken").getValue();
        log.info(String.valueOf(result.getResponse().getCookie("accessToken")));
        log.info(String.valueOf(result.getResponse().getCookie("refreshToken")));

    }
}