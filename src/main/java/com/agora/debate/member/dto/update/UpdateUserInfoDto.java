package com.agora.debate.member.dto.update;

import com.agora.debate.global.enums.Gender;
import com.agora.debate.member.valid.ValidationGroups;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoDto {
    // 닉네임: 한글, 영문, 숫자 2~15자
    @NotBlank(message = "닉네임은 반드시 입력되어야 합니다.",
            groups =  ValidationGroups.UpdateUserInfoGroup.class)
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{2,15}$",
            message = "닉네임은 한글, 영문, 숫자 조합 2~15자여야 합니다.",
            groups =  ValidationGroups.UpdateUserInfoGroup.class
    )
    private String name;

    // 이메일: '@' 포함한 일반 이메일 형식
    @NotBlank(message = "이메일은 반드시 입력되어야 합니다.",
            groups = ValidationGroups.UpdateUserInfoGroup.class)
    @Email(
            message = "올바른 이메일 형식이어야 합니다.",
            groups = {ValidationGroups.UpdateUserInfoGroup.class, ValidationGroups.UserUpdateGroup.class}
    )
    private String email;

    // 성별: null이 아니고 MALE 또는 FEMALE만 가능
    @NotNull(
            message = "성별은 필수 선택입니다.",
            groups = {ValidationGroups.UpdateUserInfoGroup.class, ValidationGroups.UserUpdateGroup.class}
    )
    private Gender gender;

    // 생년월일: 과거 날짜만 허용
    @NotNull(
            message = "생년월일은 필수입니다.",
            groups = ValidationGroups.UpdateUserInfoGroup.class
    )
    @PastOrPresent(
            message = "생년월일은 과거 날짜여야 합니다.",
            groups = ValidationGroups.UpdateUserInfoGroup.class
    )
    private LocalDate birthday;
}
