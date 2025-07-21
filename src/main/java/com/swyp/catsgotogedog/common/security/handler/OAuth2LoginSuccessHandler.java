package com.swyp.catsgotogedog.common.security.handler;

import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import com.swyp.catsgotogedog.User.service.RefreshTokenService;
import com.swyp.catsgotogedog.common.security.service.PrincipalDetails;
import com.swyp.catsgotogedog.common.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtil jwt;
    private final RefreshTokenService rtService;
    private final UserRepository userRepo;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication auth) {

//        String principal = auth.getName();
//        User user = userRepo.findByUserId(principal).orElseThrow();

        PrincipalDetails pd = (PrincipalDetails) auth.getPrincipal();
        String providerId = pd.getProviderId();

        User user = userRepo.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalStateException("회원이 없습니다"));

        String access  = jwt.createAccessToken(String.valueOf(user.getUserId()));
        String refresh = jwt.createRefreshToken(String.valueOf(user.getUserId()));

        rtService.save(user, refresh, jwt.getRefreshTokenExpiry());

        response.setHeader("Authorization",   "Bearer " + access);
        response.setHeader("X-Refresh-Token", refresh);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}