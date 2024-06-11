package com.example.demo.member.service;


import com.example.demo.global.exceptions.BusinessException;
import com.example.demo.global.exceptions.ErrorCode;
import com.example.demo.member.dto.MemberType;
import com.example.demo.member.dto.RequestJoin;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberSignupService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public void save(RequestJoin user) {

        if (memberRepository.findByEmail(user.email()).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_EMAIL);
        }

        Member member = Member.builder()
                .email(user.email())
                .password(passwordEncoder.encode(user.password()))
                .mobile(user.mobile())
                .type(MemberType.USER)
                .build();
        memberRepository.save(member);
    }


}
