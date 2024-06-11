package com.example.demo.global.jwt;

import com.example.demo.member.MemberInfo;
import com.example.demo.member.service.MemberInfoService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * TokenProvider 클래스는 JWT 토큰의 생성, 유효성 검증 및 인증 정보 추출을 담당합니다.
 */

@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long tokenValidityInSeconds;

    private Key key;

    @Autowired
    private MemberInfoService memberInfoService;

    /**
     * TokenProvider 생성자.
     *
     * @param secret Base64 인코딩된 시크릿 키
     * @param tokenValidityInSeconds 토큰의 유효 기간 (초 단위)
     */
    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.accessTokenValidityInSeconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInSeconds = tokenValidityInSeconds;
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 인증 정보를 기반으로 JWT 토큰을 생성합니다.
     *
     * @param authentication 인증 정보
     * @return 생성된 JWT 토큰
     */
    public String createToken(Authentication authentication) {
        // 인증 정보에서 권한을 추출하여 문자열로 변환
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date().getTime());
        Date validity = new Date(now + this.tokenValidityInSeconds * 1000);

        // JWT 토큰 생성
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * JWT 토큰을 이용해 인증 정보를 가져옵니다.
     *
     * @param token JWT 토큰
     * @return 인증 정보
     */
    public Authentication getAuthentication(String token) {
        // 토큰을 파싱하여 클레임을 가져옴
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getPayload();

        // 클레임에서 권한 정보를 추출하여 GrantedAuthority 리스트로 변환
        List<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        // 클레임에서 주체 (username)를 이용해 사용자 정보 로드
        MemberInfo memberInfo = (MemberInfo)memberInfoService.loadUserByUsername(claims.getSubject());
        memberInfo.setAuthorities(authorities);

        // 인증 토큰 생성
        return new UsernamePasswordAuthenticationToken(memberInfo, token, authorities);
    }


    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getPayload();

        return claims.getSubject();
    }
        /**
         * JWT 토큰의 유효성을 검증합니다.
         *
         * @param token JWT 토큰
         * @return 토큰이 유효한지 여부
         */
    public boolean validateToken(String token) {
        try {
            // 토큰 파싱하여 클레임을 가져옴
            Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return true; // 토큰이 유효하면 true 반환
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다."); // 잘못된 서명 예외 처리
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다."); // 만료된 토큰 예외 처리
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰 입니다."); // 지원되지 않는 토큰 예외 처리
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다."); // 잘못된 토큰 예외 처리
            e.printStackTrace();
        }

        return false; // 토큰이 유효하지 않으면 false 반환
    }
}
