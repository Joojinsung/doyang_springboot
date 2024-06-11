package com.example.demo.post.repository;

import com.example.demo.post.dto.UpdateRequest;
import com.example.demo.post.entity.QPostEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public String updatePost(Long memberId, UpdateRequest request) {
        QPostEntity post = QPostEntity.postEntity;

        long updateCount = queryFactory.update(post)
                .set(post.title, request.title())
                .set(post.content, request.content())
                .set(post.usePassword, request.usePassword())
                .where(
                        post.id.eq(request.postId()),
                        post.member.id.eq(memberId)
                )
                .execute();

        if (updateCount > 0) {
            return "수정완료";
        } else {
            return "수정할 포스트를 찾을 수 없음";
        }
    }
}