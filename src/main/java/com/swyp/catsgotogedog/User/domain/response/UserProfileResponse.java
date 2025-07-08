package com.swyp.catsgotogedog.User.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private String loginId;
    private String name;
    private String birth;
    private String phoneNumber;
    private String email;
    private String address;
    private String visaType;
    private Integer age;
    private String education;
    private String experience;
    private String koreanProficiency;
    private String region;
    private String visaDescription;
}