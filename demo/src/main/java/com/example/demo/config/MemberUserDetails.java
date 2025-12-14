package com.example.demo.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.model.domain.Member;

/**
 * Spring Security 인증용 사용자 어댑터.
 * Member 엔티티를 Security의 UserDetails로 감싸서 인증 정보와 화면 표시용 이름을 함께 제공한다.
 */
public class MemberUserDetails implements UserDetails {

    private final Member member;
    private final Collection<? extends GrantedAuthority> authorities;

    private MemberUserDetails(Member member) {
        this.member = member;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public static MemberUserDetails from(Member member) {
        return new MemberUserDetails(member);
    }

    public String getDisplayName() {
        return member.getName();
    }

    public String getEmail() {
        return member.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
