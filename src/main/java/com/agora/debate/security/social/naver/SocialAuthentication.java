package com.agora.debate.security.social.naver;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@Getter
@Setter
public class SocialAuthentication implements Authentication {

    private final String name;
    private boolean authenticated = false;
    private final Collection<? extends GrantedAuthority> authorities;

    public SocialAuthentication(String name) {
        this.name = name;
        this.authorities = null;
    }

    public SocialAuthentication(String name, Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.authorities = authorities;
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null; // 비밀번호 없음
    }

    @Override
    public Object getDetails() {
        return null; // 필요 시 확장 가능
    }

    @Override
    public Object getPrincipal() {
        return name; // 유저 식별자
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return name;
    }
}