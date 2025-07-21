package com.swyp.catsgotogedog.User.controller;

import com.swyp.catsgotogedog.User.domain.AccessTokenResponse;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import com.swyp.catsgotogedog.User.service.RefreshTokenService;
import com.swyp.catsgotogedog.User.service.UserService;
import com.swyp.catsgotogedog.common.util.JwtTokenUtil;
import com.swyp.catsgotogedog.global.CatsgotogedogApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController implements UserControllerSwagger{

    private final JwtTokenUtil jwt;
    private final RefreshTokenService rtService;
    private final UserService userService;
    private final UserRepository userRepo;

    @PostMapping("/reissue")
    public ResponseEntity<CatsgotogedogApiResponse<?>> reIssue(
            @CookieValue("X-Refresh-Token") String refresh) {

        return ResponseEntity.ok(CatsgotogedogApiResponse.success("재발급 성공",
            new AccessTokenResponse(userService.reIssue(refresh))));
    }

    @PostMapping("/logout")
    public ResponseEntity<CatsgotogedogApiResponse<?>> logout(
            @CookieValue("X-Refresh-Token") String refresh) {

        userService.logout(refresh);

        return ResponseEntity.ok(CatsgotogedogApiResponse.success("로그아웃 성공", null));
    }
}