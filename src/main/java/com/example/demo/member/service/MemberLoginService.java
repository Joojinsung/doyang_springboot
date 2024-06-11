package com.example.demo.member.service;


import com.example.demo.global.jwt.TokenProvider;
import com.example.demo.member.dto.RequestLogin;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberLoginService {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final HttpServletResponse response;

    public String authenticate(RequestLogin user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.email(), user.password());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 인증 정보를 가지고 JWT AccessToken 발급
        String accessToken = tokenProvider.createToken(authentication);

        // 쿠키에 액세스 토큰 담기
        Cookie cookie = new Cookie("accessToken", accessToken);
        String tokenKey = cookie.getName(); // 토큰 키 가져오기

        // 토큰 키와 값 로그 출력
        log.info("Token Key: {}", tokenKey);
        log.info("Token Value: {}", accessToken);


        response.addCookie(cookie);


        return accessToken;
    }

    private ResponseCookie getAccessToken(String accessToken) {
        ResponseCookie cookie = ResponseCookie.from("ACCESSTOKEN", accessToken)
                .maxAge(60 * 60)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // sameSite 정책을 None 으로 설정
                .domain("localhost")
                .path("/")
                .build();
        return cookie;
    }
}
