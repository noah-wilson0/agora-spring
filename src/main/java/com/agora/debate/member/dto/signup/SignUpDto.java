package com.agora.debate.member.dto.signup;

import com.agora.debate.global.enums.Gender;
import com.agora.debate.member.valid.ValidationGroups;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpDto {

    // 닉네임: 한글, 영문, 숫자 2~15자
    @NotBlank(message = "닉네임은 반드시 입력되어야 합니다.",
            groups = {ValidationGroups.SignUpGroup.class, ValidationGroups.UserUpdateGroup.class})
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{2,15}$",
            message = "닉네임은 한글, 영문, 숫자 조합 2~15자여야 합니다.",
            groups = {ValidationGroups.SignUpGroup.class, ValidationGroups.UserUpdateGroup.class}
    )
    private String name;

    // 아이디: 영문 + 숫자 6~12자
    @NotBlank(message = "아이디는 반드시 입력되어야 합니다.",
            groups = ValidationGroups.SignUpGroup.class)
    @Pattern(
            regexp = "^[a-zA-Z0-9]{6,12}$",
            message = "아이디는 영문과 숫자 조합 6~12자여야 합니다.",
            groups = ValidationGroups.SignUpGroup.class
    )
    private String loginId;

    // 비밀번호: 영문, 숫자, 특수문자 포함 8~15자
    @NotBlank(message = "비밀번호는 반드시 입력되어야 합니다.",
            groups = ValidationGroups.SignUpGroup.class)
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=\\+{}\\[\\]:;,.?/])[A-Za-z\\d!@#$%^&*()\\-_=\\+{}\\[\\]:;,.?/]{8,15}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~15자여야 합니다.",
            groups = {ValidationGroups.SignUpGroup.class, ValidationGroups.UserUpdateGroup.class}
    )
    private String password;

    // 이메일: '@' 포함한 일반 이메일 형식
    @NotBlank(message = "이메일은 반드시 입력되어야 합니다.",
            groups = ValidationGroups.SignUpGroup.class)
    @Email(
            message = "올바른 이메일 형식이어야 합니다.",
            groups = {ValidationGroups.SignUpGroup.class, ValidationGroups.UserUpdateGroup.class}
    )
    private String email;

    // 성별: null이 아니고 MALE 또는 FEMALE만 가능
    @NotNull(
            message = "성별은 필수 선택입니다.",
            groups = {ValidationGroups.SignUpGroup.class, ValidationGroups.UserUpdateGroup.class}
    )
    private Gender gender;

    // 생년월일: 과거 날짜만 허용
    @NotNull(
            message = "생년월일은 필수입니다.",
            groups = {ValidationGroups.SignUpGroup.class, ValidationGroups.UserUpdateGroup.class}
    )
    @PastOrPresent(
            message = "생년월일은 과거 날짜여야 합니다.",
            groups = {ValidationGroups.SignUpGroup.class, ValidationGroups.UserUpdateGroup.class}
    )
    private LocalDate birthday;
}
