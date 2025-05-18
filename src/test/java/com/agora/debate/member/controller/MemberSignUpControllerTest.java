package com.agora.debate.member.controller;

import com.agora.debate.global.enums.Gender;
import com.agora.debate.member.dto.signup.SignUpDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class MemberSignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);




    @DisplayName("유효성_검사_실패_닉네임_없음")
    @WithMockUser
    @Test
    void createMemberNameError() throws Exception {
        SignUpDto dto = SignUpDto.builder()
                .loginId("test1234")
                .password("test1234678@")
                .email("test@naver.com")
                .gender(Gender.MALE)
                .birthday(LocalDate.parse("2001-08-03"))
                .build();

        MvcResult response = mockMvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andReturn();

        int actualStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();
        log.info("응답 상태: {}", actualStatus);
        log.info("응답 본문: {}", responseBody);
    }

    @DisplayName("유효성_검사_성공")
    @WithMockUser
    @Test
    void createMember() throws Exception {
        SignUpDto dto = SignUpDto.builder()
                .name("Test")
                .loginId("test1234")
                .password("test1234678@")
                .email("test@naver.com")
                .gender(Gender.MALE)
                .birthday(LocalDate.parse("2001-08-03"))
                .build();

        MvcResult response = mockMvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andReturn();

        int actualStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();
        log.info("응답 상태: {}", actualStatus);
        log.info("응답 본문: {}", responseBody);
    }
}
