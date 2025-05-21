package com.agora.debate.member.dto.profile;

import com.agora.debate.global.enums.Gender;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberProfileResponse {
    private Long id;
    private String name;
    private String email;
    private Gender gender;
    private int score;
    private int level;
    private int win;
    private int lose;
}
