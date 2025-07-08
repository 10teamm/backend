package com.swyp.catsgotogedog.User.domain.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    private String loginId;
    private String password;
    private Date birth;
    private String phoneNumber;
    private String email;
    private UserRole role;
    private String address;
    private String visaType;
    private Integer age;
    private String education;
    private String experience;
    private String koreanProficiency;
    private String region;
    private String visaDescription;
}