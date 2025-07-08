package com.swyp.catsgotogedog.User.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.domain.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "이름이 비어있습니다.")
    private String name;

    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String loginId;

    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String password;
    private String passwordCheck;

    private String address;
    private String visaType;

    private String email;
    private String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date birth;


    public User toEntity(String encodedPassword) {
        return User.builder()
                .name(this.name)
                .loginId(this.loginId)
                .password(encodedPassword)
                .birth(this.birth)
                .address(this.address)
                .visaType(this.visaType)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .role(UserRole.USER)
                .build();
    }
}

