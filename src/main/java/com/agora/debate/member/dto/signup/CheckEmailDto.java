package com.agora.debate.member.dto.signup;

import com.agora.debate.member.valid.ValidationGroups;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckEmailDto {

    // 이메일: '@' 포함한 일반 이메일 형식
    @NotBlank(message = "이메일은 반드시 입력되어야 합니다.",
            groups = ValidationGroups.CheckEmailGroup.class)
    @Email(
            message = "올바른 이메일 형식이어야 합니다.",
            groups = ValidationGroups.CheckEmailGroup.class
    )
    private String email;
}
