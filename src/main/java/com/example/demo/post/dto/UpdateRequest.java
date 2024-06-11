package com.example.demo.post.dto;

public record UpdateRequest(
        Long postId,
        String title,
        String content,
        Boolean usePassword
) {
}
