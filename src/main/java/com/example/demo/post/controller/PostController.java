package com.example.demo.post.controller;


import com.example.demo.member.MemberInfo;
import com.example.demo.post.dto.PostRequest;
import com.example.demo.post.dto.UpdateRequest;
import com.example.demo.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@Slf4j
public class PostController {
    private final PostService postService;


    @PostMapping("/createPost")
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest, @AuthenticationPrincipal MemberInfo member) {
        postService.createPost(postRequest, member);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/updatePost")
    public ResponseEntity<Void> updatePost(@AuthenticationPrincipal MemberInfo member, @RequestBody UpdateRequest request) {
        postService.updatePost(request, member.getMember().getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
