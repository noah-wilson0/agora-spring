package com.agora.debate.member.service.kakao;

import com.agora.debate.member.dto.kako.KakaoTokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KaokaoAuthService {
    @Value("${kakao.clientid}")
    private String KAKAO_CLIENT_ID;

    private final RestTemplate restTemplate;
    private String KAKAO_REDIRECT_URI = "http://localhost:5173/oauth/callback/kakao";

    // 1. AccessToken 발급
    public String getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", KAKAO_CLIENT_ID);
        body.add("redirect_uri", KAKAO_REDIRECT_URI);
        body.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, KakaoTokenResponse.class
        );

        return response.getBody().getAccess_token();

    }



    public Map<String,Object> getUserInfo(String accessToken) throws JsonProcessingException {
        String baseUrl = "https://kapi.kakao.com/v2/user/me";

        // URI 생성
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .build()
                .toUriString();
        log.info("url:{}",url);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("카카오 사용자 정보 요청 실패");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        log.info(response.getBody());
        String id = root.path("id").asText();
        JsonNode kakaoAccount = root.path("kakao_account");
        JsonNode profile = kakaoAccount.path("profile");
        String nickname = profile.path("nickname").asText(null);

        log.info("id: {}, nickname: {}", id, nickname);
        return Map.of("id", id, "nickname", nickname);
    }

}
