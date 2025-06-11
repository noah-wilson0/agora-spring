package com.agora.debate.member.entity;

import com.agora.debate.global.enums.Gender;
import com.agora.debate.global.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO: @EqualsAndHashCode(of = "id")공부
 */
@Entity
@Table(name = "member")
@Builder
@EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "role")
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    //Default=true
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }
    //Default=true
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }
    //Default=true
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }
    //Default=true
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeUsername(String newUsername) {
        this.username = newUsername;
    }
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
    public void changeEmail(String email) {
        this.email = email;
    }
    public void changeGender(Gender gender) {
        this.gender = gender;
    }
    public void changeBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
