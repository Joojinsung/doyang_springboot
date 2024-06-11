package com.example.demo.member.dto;

import lombok.Builder;

@Builder
public record ResponseLogin(
        String accessToken
) {}