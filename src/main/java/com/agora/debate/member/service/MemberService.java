package com.agora.debate.member.service;


import com.agora.debate.member.dto.signup.SignUpDto;
import com.agora.debate.member.entity.Member;
import com.agora.debate.member.repository.MemberRepository;
import com.agora.debate.member.utils.MemberPolicyValidator;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberPolicyValidator memberPolicyValidator;

    @Transactional
    public Member register(SignUpDto signDto) {
        memberPolicyValidator.validateSignUpDto(signDto);

        return memberRepository.save(Member.builder()
                .name(signDto.getName())
                .loginId(signDto.getLoginId())
                .password(signDto.getPassword())
                .email(signDto.getEmail())
                .gender(signDto.getGender())
                .birthday(signDto.getBirthday())
                .build());
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

}
