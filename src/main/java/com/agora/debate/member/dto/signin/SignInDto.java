package com.agora.debate.member.dto.signin;

import com.agora.debate.member.valid.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInDto {

    // 아이디: 영문 + 숫자 6~12자
    @NotBlank(message = "아이디는 반드시 입력되어야 합니다.",
            groups = ValidationGroups.SignUpGroup.class)
    @Pattern(
            regexp = "^[a-zA-Z0-9]{6,12}$",
            message = "아이디는 영문과 숫자 조합 6~12자여야 합니다.",
            groups = ValidationGroups.SignUpGroup.class
    )
    private String username;


    // 비밀번호: 영문, 숫자, 특수문자 포함 8~15자
    @NotBlank(message = "비밀번호는 반드시 입력되어야 합니다.",
            groups = ValidationGroups.SignUpGroup.class)
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=\\+{}\\[\\]:;,.?/])[A-Za-z\\d!@#$%^&*()\\-_=\\+{}\\[\\]:;,.?/]{8,15}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~15자여야 합니다.",
            groups = {ValidationGroups.SignUpGroup.class, ValidationGroups.UserUpdateGroup.class}
    )
    private String password;
}
