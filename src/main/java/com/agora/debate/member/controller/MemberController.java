package com.agora.debate.member.controller;


import com.agora.debate.member.dto.MemberDto;
import com.agora.debate.member.dto.profile.MemberInfo;
import com.agora.debate.member.entity.Member;
import com.agora.debate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  TODO :
 *  토큰 리프레쉬는 어떻게 하지?
 *  POST	/api/logout	인증된 사용자만 로그아웃 가능
 */
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberInfo> getMember(@AuthenticationPrincipal Member member) {

                return ResponseEntity.ok(MemberInfo.builder()
                .name(member.getName())
                .signInId(member.getUsername())
                .email(member.getEmail())
                .gender(member.getGender())
                .birthday(member.getBirthday())
                .score(member.getScore())
                .level(member.getLevel())
                .win(member.getWin())
                .lose(member.getLose())
                .build());
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(){
        return ResponseEntity.ok(null);
    }




}
