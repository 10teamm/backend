package com.swyp.catsgotogedog.common.security.handler;

import java.io.IOException;

import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import com.swyp.catsgotogedog.User.service.RefreshTokenService;
import com.swyp.catsgotogedog.common.security.service.PrincipalDetails;
import com.swyp.catsgotogedog.common.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler
	implements AuthenticationSuccessHandler {

    private final JwtTokenUtil jwt;
    private final RefreshTokenService rtService;
    private final UserRepository userRepo;

    @Value("${frontend.base.url}")
    private String frontend_base_url;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {

        PrincipalDetails pd = (PrincipalDetails) auth.getPrincipal();
        String providerId = pd.getProviderId();

        User user = userRepo.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalStateException("회원이 없습니다"));

        String access  = jwt.createAccessToken(String.valueOf(user.getUserId()));
        String refresh = jwt.createRefreshToken(String.valueOf(user.getUserId()));

        rtService.save(user, refresh, jwt.getRefreshTokenExpiry());

        String targetUrl = UriComponentsBuilder.fromUriString(frontend_base_url)
            .queryParam("accessToken", access)
            .queryParam("refreshToken", refresh)
            .build()
            .toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}