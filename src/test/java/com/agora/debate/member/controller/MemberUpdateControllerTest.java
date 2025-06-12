package com.agora.debate.member.controller;

import com.agora.debate.member.dto.signin.SignInDto;
import com.agora.debate.member.service.JwtBlacklistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODO: MemberUpdateControllerTest
 */
@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MemberUpdateControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtBlacklistService jwtBlacklistService;

    @Autowired
    RedisTemplate redisTemplate;

    private String accessToken;
    private String refreshToken;

    @BeforeEach
    void setUp() throws Exception {
        String loginJson = objectMapper.writeValueAsString(new SignInDto("test1234", "test12345678@"));
        MvcResult result = mockMvc.perform(post("/members/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andReturn();
        accessToken = result.getResponse().getCookie("accessToken").getValue();
        refreshToken = result.getResponse().getCookie("refreshToken").getValue();
    }

    @AfterEach
    void tearDown() {
        if (refreshToken != null) {
            jwtBlacklistService.addBlacklistToken(refreshToken, "test");
        }
        if (accessToken != null) {
            jwtBlacklistService.addBlacklistToken(accessToken, "test");
        }
    }

    @DisplayName("비밀번호_일치_확인_테스트")
    @Test
    void checkPassword() throws Exception {
        String s = objectMapper.writeValueAsString("test1234678@");
        mockMvc.perform(post("/members/update/change-password")
                .cookie(new Cookie("accessToken", accessToken))
                .cookie(new Cookie ("refreshToken",refreshToken)))
                .andExpect(status().isOk());

    }

    @DisplayName("유저_비밀번호_변경_테스트")
    @Test
    void updatePassword() {
    }
    @DisplayName("유저정보_수정_테스트")
    @Test
    void updateMemberInfo() {
    }
}