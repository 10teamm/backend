package com.swyp.catsgotogedog.User.domain.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    private String name;
    private String email;
    private String provider;     // google / kakao / naver
    private String providerId;

    private String profileImage;

}