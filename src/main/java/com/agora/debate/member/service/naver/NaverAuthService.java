package com.agora.debate.member.service.naver;

import com.agora.debate.member.dto.naver.NaverTokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
public class NaverAuthService {
    @Value("${naver.clientid}")
    private String NAVER_CLIENT_ID;
    @Value("${naver.clientsecret}")
    private String NAVER_CLIENT_SECRET;
    private final RestTemplate restTemplate;

    // 1. AccessToken 발급
    public String getAccessToken(String code, String state) {
        String url = "https://nid.naver.com/oauth2.0/token";

        // 파라미터 세팅
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("client_id", NAVER_CLIENT_ID);
        params.put("client_secret", NAVER_CLIENT_SECRET);
        params.put("code", code);
        params.put("state", state);

        // 헤더 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 파라미터를 form으로 변환
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (sb.length() > 0) sb.append("&");
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }

        HttpEntity<String> entity = new HttpEntity<>(sb.toString(), headers);

        ResponseEntity<NaverTokenResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, NaverTokenResponse.class);
        //네이버 RT 저장

        return response.getBody().getAccess_token();

    }


    public Map<String,Object> getUserInfo(String accessToken) throws JsonProcessingException {
        String url = "https://openapi.naver.com/v1/nid/me";

        // 1. Authorization 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        // 2. GET 요청 실행
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
        );

        // 3. 응답 검증
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("네이버 사용자 정보 요청 실패");
        }

        // 4. 응답 JSON 파싱 (Jackson 활용)
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());

        JsonNode res = root.path("response");
        String username = res.path("name").asText();
        String email = res.path("email").asText();
        String id = res.path("id").asText();
        return Map.of("id",id,"username",username,"email",email);
    }

}