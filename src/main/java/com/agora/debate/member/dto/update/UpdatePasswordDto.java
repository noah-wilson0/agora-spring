package com.agora.debate.member.dto.update;

import com.agora.debate.member.valid.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdatePasswordDto {

    // 새로운 비밀번호: 영문, 숫자, 특수문자 포함 8~15자
    @NotBlank(message = "비밀번호는 반드시 입력되어야 합니다.",
            groups = ValidationGroups.CheckPasswordGroup.class)
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=\\+{}\\[\\]:;,.?/])[A-Za-z\\d!@#$%^&*()\\-_=\\+{}\\[\\]:;,.?/]{8,15}$",
            message = "비밀번호가 일치하지 않습니다.",
            groups =  ValidationGroups.CheckPasswordGroup.class
    )
    private String newPassword;

    // 새로운 비밀번호 확인용 : 영문, 숫자, 특수문자 포함 8~15자
    @NotBlank(message = "비밀번호는 반드시 입력되어야 합니다.",
            groups = ValidationGroups.CheckPasswordGroup.class)
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=\\+{}\\[\\]:;,.?/])[A-Za-z\\d!@#$%^&*()\\-_=\\+{}\\[\\]:;,.?/]{8,15}$",
            message = "비밀번호가 일치하지 않습니다.",
            groups =  ValidationGroups.CheckPasswordGroup.class
    )
    private String confirmNewPassword;
}
