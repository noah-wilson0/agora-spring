package com.agora.debate.member.entity;

import com.agora.debate.global.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "member")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Gender gender;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthday;

    @Builder.Default
    @Column(nullable = false)
    private int score=100;

    @Builder.Default
    @Column(nullable = false)
    private int level=1;

    @Builder.Default
    @Column(nullable = false)
    private int win=0;

    @Builder.Default
    @Column(nullable = false)
    private int lose=0;

}
