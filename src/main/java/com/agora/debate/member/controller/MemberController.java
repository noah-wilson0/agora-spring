package com.agora.debate.member.controller;


import com.agora.debate.member.dto.profile.MemberInfo;
import com.agora.debate.member.entity.Member;
import com.agora.debate.security.JwtUtils;
import com.agora.debate.member.service.JwtBlacklistService;
import com.agora.debate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final JwtBlacklistService jwtBlacklistService;

    @GetMapping("/me")
    public ResponseEntity<MemberInfo> getMember(@AuthenticationPrincipal Member member) {

                return ResponseEntity.ok(MemberInfo.builder()
                .name(member.getName())
                .username(member.getUsername())
                .email(member.getEmail())
                .gender(member.getGender())
                .birthday(member.getBirthday())
                .score(member.getScore())
                .level(member.getLevel())
                .win(member.getWin())
                .lose(member.getLose())
                .build());
    }

    /**
     * TODO : AT도 httponly에서 지우기
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader,
                                    @CookieValue(value = "refreshToken", required = false) String refreshToken) {
        String accessToken = JwtUtils.extractAccessToken(authorizationHeader);

        // 2. AT 블랙리스트 등록
        if (accessToken != null) {
            jwtBlacklistService.addBlacklistToken(accessToken,"logout");
        }

        // 3. RT 블랙리스트 등록
        if (refreshToken != null) {
            jwtBlacklistService.addBlacklistToken(refreshToken,"logout");
        }
        // 4. RT 쿠키 삭제 (Set-Cookie로 만료시키기)
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .maxAge(0)
                .httpOnly(true)
                .secure(true) // 개발환경이면 false
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body("로그아웃 완료");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMember(@AuthenticationPrincipal Member member) {
        memberService.deleteMember(member);

        return ResponseEntity.ok().body("회원 탈퇴가 완료되었습니다.");
    }

}
