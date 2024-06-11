package com.example.demo.post.dto;

public record PostRequest(
        String title,
        String content,
        Boolean usePassword
) {

}
