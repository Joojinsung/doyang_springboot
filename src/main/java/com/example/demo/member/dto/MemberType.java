package com.example.demo.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MemberType {
    ADMIN("ROLE_GUEST"), USER("ROLE_USER");

    private final String key;
}
