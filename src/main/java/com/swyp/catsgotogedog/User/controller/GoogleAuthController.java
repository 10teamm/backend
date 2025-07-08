package com.swyp.catsgotogedog.User.controller;


import com.swyp.catsgotogedog.User.domain.request.GoogleOAuthRequest;
import com.swyp.catsgotogedog.User.domain.response.LoginResponse;
import com.swyp.catsgotogedog.User.service.UserService;
import com.swyp.catsgotogedog.common.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class GoogleAuthController {

    private final UserService userService;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostMapping("/google")
    public ResponseEntity<LoginResponse> googleLogin(@RequestBody GoogleOAuthRequest request) {
//        String idToken = request.getAccount().getId_token();
        String idToken = request.getIdToken();

        String loginId = userService.processGoogleIdToken(idToken);

        long expireTimeMs = 1000 * 60 * 60; // 예: 60분 유효

        String jwtToken = JwtTokenUtil.createToken(loginId, secretKey, expireTimeMs);

        LoginResponse response = new LoginResponse("로그인 성공", jwtToken);

        return ResponseEntity.ok(response);
    }
}
