package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.domain.Member;
import com.example.demo.model.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

/**
 * 로그인 시 이메일로 Member를 조회해 UserDetails로 반환한다.
 * 인증 과정의 관찰을 위해 간단한 로그를 남긴다.
 */
@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(MemberUserDetailsService.class);

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            log.warn("로그인 실패 - 이메일을 찾을 수 없음: {}", email);
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        log.info("로그인 시도 - 이메일: {}", email);
        return MemberUserDetails.from(member);
    }
}
