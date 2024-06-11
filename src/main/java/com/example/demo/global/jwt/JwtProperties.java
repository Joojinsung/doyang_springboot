package com.example.demo.global.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class JwtProperties {
    @Value("${jwt.header}")
    private String header;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.accessTokenValidityInSeconds}")
    private Long accessTokenValidityInSeconds;
}
