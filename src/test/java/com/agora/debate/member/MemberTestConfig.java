package com.agora.debate.member;

import com.agora.debate.member.repository.MemberRepository;
import com.agora.debate.member.security.JwtTokenProvider;
import com.agora.debate.member.service.MemberService;
import com.agora.debate.member.utils.MemberPolicyValidator;
import com.agora.debate.member.utils.MemberSignUpPolicyChecker;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.mock;


@TestConfiguration
public class MemberTestConfig {

    @Bean
    public MemberRepository memberRepository() {
        return mock(MemberRepository.class);
    }

    @Bean
    public MemberSignUpPolicyChecker memberSignUpPolicyChecker() {
        return new MemberSignUpPolicyChecker(memberRepository());
    }

    @Bean
    public MemberPolicyValidator memberPolicyValidator() {
        return new MemberPolicyValidator(memberSignUpPolicyChecker());
    }
    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return mock(JwtTokenProvider.class);
    }

    @Bean
    public AuthenticationManagerBuilder authenticationManagerBuilder() {
        return mock(AuthenticationManagerBuilder.class);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(
                memberRepository(),
                memberPolicyValidator(),
                jwtTokenProvider(),
                authenticationManagerBuilder(),
                passwordEncoder()
                );
    }
}
