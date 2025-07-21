package com.swyp.catsgotogedog.User.controller;

import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import com.swyp.catsgotogedog.User.service.RefreshTokenService;
import com.swyp.catsgotogedog.common.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController implements UserControllerSwagger{

    private final JwtTokenUtil jwt;
    private final RefreshTokenService rtService;
    private final UserRepository userRepo;

    @PostMapping("/reissue")
    public ResponseEntity<Void> reissue(
            @RequestHeader("X-Refresh-Token") String refresh) {

        if (!rtService.validate(refresh)) {
            return ResponseEntity.status(401).build();
        }

        int userId = Integer.parseInt(jwt.getSubject(refresh));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        String newAccess  = jwt.createAccessToken(String.valueOf(userId));
        String newRefresh = jwt.createRefreshToken(String.valueOf(userId));

        rtService.save(user, newRefresh, jwt.getRefreshTokenExpiry());

        return ResponseEntity.ok()
                .header("Authorization",   "Bearer " + newAccess)
                .header("X-Refresh-Token", newRefresh)
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("X-Refresh-Token") String refresh) {
        rtService.delete(refresh);
        return ResponseEntity.ok("로그아웃 완료");
    }
}