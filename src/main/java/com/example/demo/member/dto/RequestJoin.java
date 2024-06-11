package com.example.demo.member.dto;

public record RequestJoin(

        String email,
        String password,
        String confirmPassword,
        String nickname,
        String mobile


) {
}
