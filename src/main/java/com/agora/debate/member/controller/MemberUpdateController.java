package com.agora.debate.member.controller;

import com.agora.debate.member.dto.PasswordCheckRequestDto;
import com.agora.debate.member.dto.update.UpdatePasswordDto;
import com.agora.debate.member.dto.update.UpdateUserInfoDto;
import com.agora.debate.member.entity.Member;
import com.agora.debate.member.service.JwtBlacklistService;
import com.agora.debate.member.service.MemberService;
import com.agora.debate.member.valid.ValidationGroups;
import com.agora.debate.security.utils.JwtCookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/members/update")
@RequiredArgsConstructor
public class MemberUpdateController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtBlacklistService jwtBlacklistService;

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

    @PostMapping("/change-password")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal Member member,
            @RequestBody @Validated(ValidationGroups.UpdatePasswordGroup.class) UpdatePasswordDto updatePasswordDto,
            @CookieValue(value = "accessToken", required = false) String accessToken,
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {
        log.info("비밀번호 변경 시작");
        if (accessToken == null && refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!updatePasswordDto.getNewPassword().equals(updatePasswordDto.getConfirmNewPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        log.info("비밀번호 변경 진행");
        memberService.updatePassword(member.getUsername(), updatePasswordDto);
        log.info("비밀번호 변경 완료");
        // 2. AT 블랙리스트 등록
        if (accessToken != null) {
            jwtBlacklistService.addBlacklistToken(accessToken,"changePassword");
        }
        // 3. RT 블랙리스트 등록
        if (refreshToken != null) {
            jwtBlacklistService.addBlacklistToken(refreshToken,"changePassword");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, JwtCookieUtils.deleteAccessTokenCookie().toString())
                .header(HttpHeaders.SET_COOKIE, JwtCookieUtils.deleteRefreshTokenCookie().toString())
                .body("비밀번호 변경 완료");
    }


    @PatchMapping("/change-info")
    public ResponseEntity<?> updateMemberInfo(@AuthenticationPrincipal Member member,
                                          @RequestBody @Validated(ValidationGroups.UpdateUserInfoGroup.class) UpdateUserInfoDto updateUserInfoDto){

        memberService.updateMemberInfo(updateUserInfoDto, member);
        return ResponseEntity.ok().body("회원정보 업데이트 완료");

    }


}
