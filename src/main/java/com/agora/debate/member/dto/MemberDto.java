package com.agora.debate.member.dto;

import com.agora.debate.global.enums.Gender;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDto {

    private Long id;

    private String name;

    private String loginId;

    private String email;

    private Gender gender;

    private LocalDate birthday;

    private int score;

    private int level;

    private int win;

    private int lose;
}
