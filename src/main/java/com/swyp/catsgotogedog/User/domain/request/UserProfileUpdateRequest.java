package com.swyp.catsgotogedog.User.domain.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileUpdateRequest {
    private Integer age;
    private String education;
    private String experience;
    private String koreanProficiency;
    private String region;
    private String visaDescription;
}
