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
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtil jwt;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication auth) {

        String principal = auth.getName();
        String access  = jwt.createAccessToken(principal);
        String refresh = jwt.createRefreshToken(principal);

        response.setHeader("Authorization",   "Bearer " + access);
        response.setHeader("X-Refresh-Token", refresh);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}