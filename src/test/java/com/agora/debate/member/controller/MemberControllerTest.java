package com.agora.debate.member.controller;

import com.agora.debate.member.dto.signin.SignInDto;
import com.agora.debate.member.service.JwtBlacklistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

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

    @DisplayName("회원정보_조회_성공_테스트")
    @Test
    void getMember() throws Exception {
        MvcResult result = mockMvc.perform(get("/members/me")
                        .cookie(new Cookie("accessToken", accessToken)))
                .andExpect(status().isOk())
                .andReturn();
        log.info("회원정보:{}",result.getResponse().getContentAsString());

    }

    @DisplayName("회원정보_조회_실패_AT 유효성_실패_테스트")
    @Test
    void failedGetMember() throws Exception {
        MvcResult result = mockMvc.perform(get("/members/me")
                        .cookie(new Cookie("accessToken", accessToken+"test")))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @DisplayName("로그아웃_성공_테스트")
    @Test
    void logout() throws Exception {
        mockMvc.perform(post("/members/logout")
                        .cookie(new Cookie("accessToken", accessToken),
                                new Cookie("refreshToken", refreshToken)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, org.hamcrest.Matchers.containsString("Max-Age=0")))
                .andReturn();

        Assertions.assertTrue(redisTemplate.hasKey("blacklist:" + accessToken));
    }
    @DisplayName("로그아웃_실패_AT 유효성_테스트")
    @Test
    void failedLogout() throws Exception {
        mockMvc.perform(post("/members/logout")
                        .cookie(new Cookie("accessToken", accessToken+"test"),
                                new Cookie("refreshToken", refreshToken)))
                .andExpect(status().isUnauthorized())
                .andReturn();

        Assertions.assertFalse(redisTemplate.hasKey("blacklist:" + accessToken));
    }

    /**
     * TODO: 회원탈퇴 테스트
     */
    @Test
    void deleteMember() {

    }
}