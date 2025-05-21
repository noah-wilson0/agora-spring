package com.agora.debate.member.service;


import com.agora.debate.global.exception.member.UserNameNotMatchException;
import com.agora.debate.member.dto.signin.SignInDto;
import com.agora.debate.member.dto.signup.SignUpDto;
import com.agora.debate.member.entity.Member;
import com.agora.debate.member.repository.MemberRepository;
import com.agora.debate.member.security.JwtTokenProvider;
import com.agora.debate.member.security.dto.JwtToken;
import com.agora.debate.member.utils.MemberPolicyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberPolicyValidator memberPolicyValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    public Member register(SignUpDto signDto) {
        memberPolicyValidator.validateSignUpDto(signDto);

        return memberRepository.save(Member.builder()
                .name(signDto.getName())
                .username(signDto.getUsername())
                .password(passwordEncoder.encode(signDto.getPassword()))
                .email(signDto.getEmail())
                .gender(signDto.getGender())
                .birthday(signDto.getBirthday())
                .build());
    }
    @Transactional
    public JwtToken signIn(SignInDto signInDto) {
        memberRepository.findByUsername(signInDto.getUsername())
                .orElseThrow(() -> new UserNameNotMatchException());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInDto.getUsername(), signInDto.getPassword());

        Authentication authentication = null;

        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("authentication실패");
        }
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        return jwtToken;
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

}
