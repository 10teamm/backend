package com.swyp.catsgotogedog.common.security.handler;

import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.domain.entity.UserRole;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import com.swyp.catsgotogedog.common.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final int EXPIRATION_MS = 60 * 60 * 1000;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req,
                                        HttpServletResponse res,
                                        Authentication auth) throws IOException {

        OAuth2User oAuth2 = (OAuth2User) auth.getPrincipal();
        String provider   = auth.getAuthorities().stream()
                .findFirst().orElseThrow().getAuthority();

        String providerId;
        String name;
        String email;

        switch (provider) {
            case "google" -> {
                providerId = (String) oAuth2.getAttribute("sub");
                name       = (String) oAuth2.getAttribute("name");
                email      = (String) oAuth2.getAttribute("email");
            }
            case "naver" -> {
                providerId = (String) oAuth2.getAttribute("id");
                name       = (String) oAuth2.getAttribute("name");
                email      = (String) oAuth2.getAttribute("email");
            }
            case "kakao" -> {
                providerId = (String)(oAuth2.getAttribute("id"));
                name       = (String) oAuth2.getAttribute("nickname");
                email      = (String) oAuth2.getAttribute("email");
            }
            default       -> throw new IllegalArgumentException("지원되지 않는 Provider: " + provider);
        }

        if (providerId == null || name == null) {
            throw new IllegalStateException("OAuth2 파싱 실패");
        }

        String loginId = provider + "_" + providerId;

        User user = userRepository.findByLoginId(loginId)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .loginId(loginId)
                                .nickname(name)
                                .email(email)
                                .provider(provider)
                                .providerId(providerId)
                                .role(UserRole.USER)
                                .password("")
                                .build()));

        String jwt = JwtTokenUtil.createToken(user.getLoginId(), jwtSecret, EXPIRATION_MS);

        res.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        res.sendRedirect("/");
    }
}