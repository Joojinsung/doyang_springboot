package com.example.demo.post.entity;

import com.example.demo.config.BaseEntity;
import com.example.demo.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String content;

    @Column(name = "use_password", nullable = false)
    private Boolean usePassword;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public PostEntity(String title, String content, Boolean usePassword, Member member) {
        this.title = title;
        this.content = content;
        this.usePassword = usePassword;
        this.member = member;
    }

    public void updatePost(String title, String content, Boolean usePassword) {
        this.title = title;
        this.content = content;
        this.usePassword = usePassword;
    }
}
