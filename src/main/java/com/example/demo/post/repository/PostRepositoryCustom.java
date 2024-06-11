package com.example.demo.post.repository;


import com.example.demo.post.dto.UpdateRequest;

public interface PostRepositoryCustom  {
    String updatePost(Long memberId, UpdateRequest request);
}
