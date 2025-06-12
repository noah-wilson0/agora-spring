package com.agora.debate.member.dto.profile;

import com.agora.debate.global.enums.Gender;
import com.agora.debate.global.enums.SocialType;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberInfo {
    private SocialType socialType;
    private String name;
    private String id;
    private String email;
    private Gender gender;
    private LocalDate birthday;
    private int score;
    private int level;
    private int win;
    private int lose;
}
