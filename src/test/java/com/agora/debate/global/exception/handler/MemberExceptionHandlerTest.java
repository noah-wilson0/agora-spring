package com.agora.debate.global.exception.handler;

import com.agora.debate.global.enums.Gender;
import com.agora.debate.member.dto.signup.SignUpDto;
import com.agora.debate.member.entity.Member;
import com.agora.debate.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class MemberExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;



    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeEach
    void setup() throws Exception{
        SignUpDto dto = SignUpDto.builder()
                .name("중복테스트1")
                .loginId("test12345")  // ← DB에 이미 존재하는 값
                .password("test1234678@1")
                .email("new@naver.com1")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(2001, 8, 3))
                .build();

        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andReturn();
    }

    @DisplayName("DuplicateMemberException 핸들러 테스트 - 아이디 중복")
    @WithMockUser
    @Test
    void handleDuplicate() throws Exception {
        // 중복 loginId 전송
        SignUpDto dto = SignUpDto.builder()
                .name("중복테스트2")
                .loginId("test12345")  // ← DB에 이미 존재하는 값
                .password("test1234678@2")
                .email("new@naver.com2")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(2001, 8, 3))
                .build();

        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isConflict()) // 409 기대
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertThat(response).contains("이미 존재하는 아이디");
                });


    }
}
