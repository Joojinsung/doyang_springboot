package com.example.demo.post.service;


import com.example.demo.global.exceptions.BusinessException;
import com.example.demo.global.exceptions.ErrorCode;
import com.example.demo.member.MemberInfo;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.post.dto.PostRequest;
import com.example.demo.post.dto.UpdateRequest;
import com.example.demo.post.entity.PostEntity;
import com.example.demo.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public void createPost(PostRequest request, MemberInfo user) {
        memberRepository.findById(user.getMember().getId()).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        PostEntity post = PostEntity.builder()
                .title(request.title())
                .content(request.content())
                .usePassword(request.usePassword())
                .member(user.getMember())
                .build();

        postRepository.save(post);
    }

    @Transactional
    public void updatePost(UpdateRequest request, Long userId) {
        postRepository.updatePost(userId, request);

    }
}