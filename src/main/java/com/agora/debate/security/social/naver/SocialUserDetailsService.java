package com.agora.debate.security.social.naver;

import com.agora.debate.member.entity.Member;
import com.agora.debate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SocialUserDetailsService {

    private final MemberRepository memberRepository;

    public UserDetails loadUserByUserName(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("소셜 로그인 사용자 정보 없음"));

        return User.builder()
                .username(member.getUsername())
                .password("") //  비밀번호는 검증 안 하므로 빈 값
                .roles(member.getRoles().toArray(new String[0]))
                .build();
    }
}

