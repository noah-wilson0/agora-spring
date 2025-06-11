package com.agora.debate.member.service.naver;

import com.agora.debate.global.enums.Gender;
import com.agora.debate.global.enums.SocialType;
import com.agora.debate.member.entity.Member;
import com.agora.debate.member.repository.MemberRepository;
import com.agora.debate.security.JwtTokenProvider;
import com.agora.debate.security.dto.JwtToken;
import com.agora.debate.security.social.naver.SocialAuthentication;
import com.agora.debate.security.social.naver.SocialUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class NaverService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final SocialUserDetailsService socialUserDetailsService;


    @Transactional
    public JwtToken signIn(Map<String,Object> userInfo) {
        Member member = memberRepository.findByUsername(userInfo.get("email").toString()+userInfo.get("id").toString())
                .orElseGet(() -> {
                    // └── [없음] → 회원 등록 후 로그인 진행
                    log.info("소셜 회원가입 진행");
                    return memberRepository.save(Member.builder()
                            .socialType(SocialType.NAVER)
                            .name(userInfo.get("username").toString())
                            .username(userInfo.get("email").toString()+userInfo.get("id").toString())
                            .password("") // 소셜 로그인은 비번 없음
                            .email(userInfo.get("email").toString())
                            .gender(Gender.MALE)
                            .birthday(LocalDate.now()) // 실 데이터 매핑 가능 시 수정
                            .roles(List.of("USER"))
                            .build());
                });

        log.info("Authentication생성시작");
        UserDetails userDetails = socialUserDetailsService.loadUserByUserName(userInfo.get("email").toString()+userInfo.get("id").toString());
        Authentication authentication = new SocialAuthentication(userDetails.getUsername(),userDetails.getAuthorities());
        log.info("Authentication생성 완료");
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        log.info("jwt생성완료");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //redis RT 저장
        redisTemplate.opsForValue().set(
                "refresh:" + authentication.getName(), // username
                jwtToken.getRefreshToken(),
                86400000,
                TimeUnit.MILLISECONDS
        );
        return jwtToken;
    }





}



