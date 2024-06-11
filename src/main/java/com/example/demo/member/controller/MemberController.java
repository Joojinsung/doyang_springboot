package com.example.demo.member.controller;

import com.example.demo.member.MemberInfo;

import com.example.demo.member.dto.RequestJoin;
import com.example.demo.member.dto.RequestLogin;
import com.example.demo.member.service.MemberLoginService;
import com.example.demo.member.service.MemberSignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberSignupService memberRepository;
    private final MemberLoginService loginService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody RequestJoin join) {
        memberRepository.save(join);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RequestLogin user) {
        String token = loginService.authenticate(user);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @GetMapping("/member_only")
    public void MemberOnlyUrl(@AuthenticationPrincipal MemberInfo member) {
            log.info("회원 전용 URL 접근 테스트");
        log.info(member.getEmail());
    }

    @GetMapping("/admin_only")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void adminOnlyUrl() {
        log.info("관리자 전용 URL 접근 테스트");
    }
}


