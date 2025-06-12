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
public class CheckUsernameDto {

    // 아이디: 영문 + 숫자 6~12자
    @NotBlank(message = "아이디는 반드시 입력되어야 합니다.",
            groups = ValidationGroups.CheckUsernameGroup.class)
    @Pattern(
            regexp = "^[a-zA-Z0-9]{6,12}$",
            message = "아이디는 영문과 숫자 조합 6~12자여야 합니다.",
            groups = ValidationGroups.CheckUsernameGroup.class
    )
    String id;
}
