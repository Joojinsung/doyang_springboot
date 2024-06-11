package com.example.demo.member.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestLogin(
        @NotBlank @Email
        String email,

        @NotBlank
        String password
) {}