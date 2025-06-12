package com.agora.debate.member.controller.auth;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

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

    /** TODO: jwt 재생성시 동일한 값 문제
     * jwt clams의 sub, exp, auth가 같으므로 같은 값의 jwt가 생성될떄도 있었음
     * 랜덤값을 clams에 추가하여 수정할 필요가 있음
     * @throws Exception
     */
    @DisplayName("AT_리프레쉬_테스트")
    @Test
    void refreshAccessToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/refresh")
                        .cookie(new Cookie("accessToken", accessToken))
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andExpect(status().isOk())
                .andReturn();
        String newAccessToken = result.getResponse().getCookie("accessToken").getValue();
        Assertions.assertNotEquals(accessToken,newAccessToken);
    }

    @DisplayName("AT_리프레쉬토큰_조작_실패_테스트")
    @Test
    void failedRefreshAccessToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/refresh")
                        .cookie(new Cookie("accessToken", accessToken))
                        .cookie(new Cookie("refreshToken", refreshToken+"test")))
                .andExpect(status().isBadRequest())
                .andReturn();

    }
}