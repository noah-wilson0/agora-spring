package com.agora.debate.member.service;


import com.agora.debate.global.enums.SocialType;
import com.agora.debate.member.dto.signup.CheckEmailDto;
import com.agora.debate.member.dto.signup.CheckNameDto;
import com.agora.debate.member.dto.signup.CheckUsernameDto;
import com.agora.debate.member.exception.UserNameNotMatchException;
import com.agora.debate.member.dto.signin.SignInDto;
import com.agora.debate.member.dto.signup.SignUpDto;
import com.agora.debate.member.dto.update.UpdatePasswordDto;
import com.agora.debate.member.dto.update.UpdateUserInfoDto;
import com.agora.debate.member.entity.Member;
import com.agora.debate.member.repository.MemberRepository;
import com.agora.debate.security.JwtTokenProvider;
import com.agora.debate.security.dto.JwtToken;
import com.agora.debate.member.utils.MemberPolicyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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
import java.util.concurrent.TimeUnit;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberPolicyValidator memberPolicyValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate redisTemplate;
    @Transactional
    public Member register(SignUpDto signDto) {
        memberPolicyValidator.duplicateSignUpDto(signDto);

        return memberRepository.save(Member.builder()
                        .socialType(SocialType.NORMAL)
                .name(signDto.getName())
                .username(signDto.getUsername())
                .password(passwordEncoder.encode(signDto.getPassword()))
                .email(signDto.getEmail())
                .gender(signDto.getGender())
                .birthday(signDto.getBirthday())
                .roles(List.of("USER"))
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

        //redis RT 저장
        redisTemplate.opsForValue().set(
                "refresh:" + authentication.getName(), // username
                jwtToken.getRefreshToken(),
                86400000,
                TimeUnit.MILLISECONDS
        );
        return jwtToken;
    }
    @Transactional
    public void updatePassword(String username, UpdatePasswordDto updatePasswordDto) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UserNameNotMatchException());
        member.changePassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
    }
    @Transactional
    public void updateMemberInfo(UpdateUserInfoDto updateUserInfoDto,Member member) {
        Member findMember = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new UserNameNotMatchException());
        findMember.changeName(updateUserInfoDto.getName());
        findMember.changeEmail(updateUserInfoDto.getEmail());
        findMember.changeGender(updateUserInfoDto.getGender());
        findMember.changeBirthday(updateUserInfoDto.getBirthday());
    }
    @Transactional
    public void deleteMember(Member member) {
        Member findMember = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new UserNameNotMatchException());
        memberRepository.deleteByUsername(findMember.getUsername());

    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }


    public void duplicateName(CheckNameDto checkNameDto) {
        memberPolicyValidator.duplicateName(checkNameDto);

    }

    public void duplicateUserName(CheckUsernameDto checkUsernameDto) {
        memberPolicyValidator.duplicateUserName(checkUsernameDto);
    }

    public void duplicateEmail(CheckEmailDto checkEmailDto) {
        memberPolicyValidator.duplicateEmail(checkEmailDto);

    }
}
