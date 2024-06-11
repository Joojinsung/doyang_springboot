package com.example.demo.member.entity;



import com.example.demo.config.BaseEntity;
import com.example.demo.member.dto.MemberType;
import com.example.demo.post.entity.PostEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String email;

    private String password;
    private String mobile;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MemberType type = MemberType.USER;

    @OneToMany(mappedBy = "id")
    List<PostEntity> postBoard = new ArrayList<>();
}
