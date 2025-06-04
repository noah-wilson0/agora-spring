package com.agora.debate.member.controller;


import com.agora.debate.member.dto.profile.MemberInfo;
import com.agora.debate.member.entity.Member;
import com.agora.debate.member.service.JwtBlacklistService;
import com.agora.debate.member.service.MemberService;
import com.agora.debate.security.utils.JwtCookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
                        .id(member.getUsername())
                        .email(member.getEmail())
                        .gender(member.getGender())
                        .birthday(member.getBirthday())
                        .score(member.getScore())
                        .level(member.getLevel())
                        .win(member.getWin())
                        .lose(member.getLose())
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                    @CookieValue(value = "accessToken", required = false) String accessToken) {
        if (accessToken == null && refreshToken == null) {
            log.info("로그아웃 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("로그아웃 진행");

        // 2. AT 블랙리스트 등록
        if (accessToken != null) {
            jwtBlacklistService.addBlacklistToken(accessToken,"logout");
        }

        // 3. RT 블랙리스트 등록
        if (refreshToken != null) {
            jwtBlacklistService.addBlacklistToken(refreshToken,"logout");
        }
        log.info("로그아웃 완료");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, JwtCookieUtils.deleteAccessTokenCookie().toString())
                .header(HttpHeaders.SET_COOKIE, JwtCookieUtils.deleteRefreshTokenCookie().toString())
                .body("로그아웃 완료");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMember(@AuthenticationPrincipal Member member,
                                          @CookieValue(value = "refreshToken", required = false) String refreshToken,
                                          @CookieValue(value = "accessToken", required = false) String accessToken) {
        if (accessToken == null && refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. AT 블랙리스트 등록
        if (accessToken != null) {
            jwtBlacklistService.addBlacklistToken(accessToken,"logout");
        }

        // 3. RT 블랙리스트 등록
        if (refreshToken != null) {
            jwtBlacklistService.addBlacklistToken(refreshToken,"logout");
        }

        memberService.deleteMember(member);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, JwtCookieUtils.deleteAccessTokenCookie().toString())
                .header(HttpHeaders.SET_COOKIE, JwtCookieUtils.deleteRefreshTokenCookie().toString())
                .body("회원 탈퇴가 완료되었습니다.");
    }

}
