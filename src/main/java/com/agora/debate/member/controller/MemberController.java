package com.agora.debate.member.controller;


import com.agora.debate.member.dto.MemberDto;
import com.agora.debate.member.entity.Member;
import com.agora.debate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  TODO :시큐리티 적용 필요
 *
 *  POST	/api/logout	인증된 사용자만 로그아웃 가능
 * GET	/api/members/me	내 정보 조회는 로그인한 사용자만 가능
 */
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long id) {
        Member member = memberService.findById(id).orElseThrow(
                ()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return ResponseEntity.ok(MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .loginId(member.getLoginId())
                .email(member.getEmail())
                .gender(member.getGender())
                .birthday(member.getBirthday())
                .score(member.getScore())
                .level(member.getLevel())
                .win(member.getWin())
                .lose(member.getLose())
                .build());
    }



}
