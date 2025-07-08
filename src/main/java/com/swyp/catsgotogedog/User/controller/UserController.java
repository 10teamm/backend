package com.swyp.catsgotogedog.User.controller;

import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.domain.request.JoinRequest;
import com.swyp.catsgotogedog.User.domain.request.LoginRequest;
import com.swyp.catsgotogedog.User.domain.request.UserProfileUpdateRequest;
import com.swyp.catsgotogedog.User.domain.response.LoginResponse;
import com.swyp.catsgotogedog.User.domain.response.UserProfileResponse;
import com.swyp.catsgotogedog.User.service.UserService;
import com.swyp.catsgotogedog.common.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
@Tag(name="로그인 및 회원가입 api")
public class UserController {

    private final UserService userService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Operation(summary="회원가입")
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinRequest joinRequest) {
        if (userService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("로그인 아이디가 중복됩니다.");
        }
        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("비밀번호가 일치하지 않습니다.");
        }
        userService.join(joinRequest);
        return ResponseEntity.ok("회원가입 성공");
    }

    @Operation(summary="아이디 중복체크")
    @GetMapping("/check-duplicate")
    public ResponseEntity<Map<String, Object>> checkDuplicate(@RequestParam("loginId") String loginId) {
        boolean duplicate = userService.checkLoginIdDuplicate(loginId);
        Map<String, Object> response = new HashMap<>();
        if (duplicate) {
            response.put("duplicate", true);
            response.put("message", "이미 사용중인 아이디입니다.");
        } else {
            response.put("duplicate", false);
            response.put("message", "사용 가능한 아이디입니다.");
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary="로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인 아이디 또는 비밀번호가 틀렸습니다.");
        }

        long expireTimeMs = 1000 * 60 * 60; // 60분 유효
        String jwtToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, expireTimeMs);
        LoginResponse response = new LoginResponse("로그인 성공", jwtToken);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="프로필 업데이트")
    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@RequestBody UserProfileUpdateRequest req, Authentication auth) {
        String loginId = auth.getName();
        userService.updateUserProfile(loginId, req);
        return ResponseEntity.ok("프로필 업데이트 성공");
    }

    @Operation(summary="프로필 조회")
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication) {
        String loginId = authentication.getName();
        UserProfileResponse userProfile = userService.getUserProfile(loginId);
        return ResponseEntity.ok(userProfile);
    }
}
