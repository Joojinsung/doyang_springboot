package com.example.demo.member.service;


import com.example.demo.member.MemberInfo;

import com.example.demo.member.dto.MemberType;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * MemberInfoService 클래스는 Spring Security의 UserDetailsService 인터페이스를 구현하여
 * 사용자 인증을 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    // MemberRepository를 주입받습니다. 이를 통해 데이터베이스에서 회원 정보를 조회합니다.
    private final MemberRepository memberRepository;

    /**
     * 주어진 사용자 이름(이메일)을 기반으로 사용자를 로드합니다.
     *
     * @param email 사용자의 이메일
     * @return UserDetails 인터페이스를 구현한 사용자 정보
     * @throws UsernameNotFoundException 사용자가 존재하지 않는 경우 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 데이터베이스에서 이메일로 사용자를 찾습니다.
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        // 사용자의 유형을 확인하고, null인 경우 기본값을 USER로 설정합니다.
        MemberType type = Objects.requireNonNullElse(member.getType(), MemberType.USER);

        // 사용자의 권한을 설정합니다. 여기서는 사용자의 유형을 권한으로 설정합니다.
        List<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(type.name()));

        // MemberInfo 객체를 생성하여 반환합니다.
        return MemberInfo.builder()
                .email(member.getEmail())
                .member(member)
                .authorities(authorities)
                .build();
    }
}