package com.agora.debate.member.dto.signup;

import com.agora.debate.member.valid.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckNameDto {
    // 닉네임: 한글, 영문, 숫자 2~15자
    @NotBlank(message = "닉네임은 반드시 입력되어야 합니다.",
            groups = ValidationGroups.CheckNameGroup.class)
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{2,15}$",
            message = "닉네임은 한글, 영문, 숫자 조합 2~15자여야 합니다.",
            groups = ValidationGroups.CheckNameGroup.class
    )
    String name;
}
