package com.agora.debate.member.controller;

import com.agora.debate.member.dto.PasswordCheckRequestDto;
import com.agora.debate.member.dto.update.UpdatePasswordDto;
import com.agora.debate.member.dto.update.UpdateUserInfoDto;
import com.agora.debate.member.entity.Member;
import com.agora.debate.security.JwtUtils;
import com.agora.debate.member.service.JwtBlacklistService;
import com.agora.debate.member.service.MemberService;
import com.agora.debate.member.valid.ValidationGroups;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequestMapping("/members/update")
@RequiredArgsConstructor
public class MemberUpdateController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtBlacklistService jwtBlacklistService;

    private final RedisTemplate redisTemplate;

    @PostMapping("/check-password")
    public ResponseEntity<?> checkPassword(@RequestBody @Validated(ValidationGroups.CheckPasswordGroup.class)
                                               PasswordCheckRequestDto passwordCheckRequestDto,
                                           @AuthenticationPrincipal Member member) {
        if (!passwordEncoder.matches(passwordCheckRequestDto.getPassword(), member.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("현재 비밀번호가 일치하지 않습니다.");
        }
        return ResponseEntity.ok("확인되었습니다.");

    }

    /**
     * 비밀번호 수정시 재로그인 요청하기
     * @param updatePasswordDto
     * @param authorizationHeader
     * @param refreshToken
     * @return
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> updatePassword(
            @RequestBody @Validated(ValidationGroups.UpdatePasswordGroup.class) UpdatePasswordDto updatePasswordDto,
                                            @RequestHeader("Authorization") String authorizationHeader,
                                            @CookieValue(value = "refreshToken", required = false) String refreshToken) {

        updatePasswordDto.setUsername(updatePasswordDto.getUsername());
        memberService.updatePassword(updatePasswordDto);

        String accessToken = JwtUtils.extractAccessToken(authorizationHeader);

        // 2. AT 블랙리스트 등록
        if (accessToken != null) {
            jwtBlacklistService.addBlacklistToken(accessToken,"changePassword");
        }
        // 3. RT 블랙리스트 등록
        if (refreshToken != null) {
            jwtBlacklistService.addBlacklistToken(refreshToken,"changePassword");
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
                .body("비밀번호 변경 완료");
    }


    @PatchMapping("/change-info")
    public ResponseEntity<?> updateMemberInfo(@AuthenticationPrincipal Member member,
                                          @RequestBody @Validated(ValidationGroups.UpdateUserInfoGroup.class) UpdateUserInfoDto updateUserInfoDto){

        memberService.updateMemberInfo(updateUserInfoDto, member);
        return ResponseEntity.ok().body("회원정보 업데이트 완료");
    }


}
