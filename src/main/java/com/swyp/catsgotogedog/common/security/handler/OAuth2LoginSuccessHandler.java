package com.swyp.catsgotogedog.common.security.handler;

import static com.swyp.catsgotogedog.common.security.filter.OAuth2AutoLoginFilter.*;

import java.io.IOException;
import java.time.Duration;

import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import com.swyp.catsgotogedog.User.service.RefreshTokenService;
import com.swyp.catsgotogedog.common.security.service.PrincipalDetails;
import com.swyp.catsgotogedog.common.util.JwtTokenUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

    @Value("${jwt.refresh-expire-day}")
    private int refreshDay;

    /**
     * 최초 로그인 시 RefreshToken만 Cookie로  반환하도록 설정
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {

        PrincipalDetails pd = (PrincipalDetails) auth.getPrincipal();
        String providerId = pd.getProviderId();

        User user = userRepo.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalStateException("회원이 없습니다"));

        //String access  = jwt.createAccessToken(String.valueOf(user.getUserId()), user.getEmail());
        String refresh = jwt.createRefreshToken(String.valueOf(user.getUserId()), user.getEmail());

        rtService.save(user, refresh, jwt.getRefreshTokenExpiry());

        addRefreshTokenCookie(response, refresh, isAutoLogin(request));

        // String targetUrl = UriComponentsBuilder.fromUriString(frontend_base_url)
        //     .queryParam("accessToken", access)
        //     .build()
        //     .toUriString();
        getRedirectStrategy().sendRedirect(request, response, frontend_base_url);
    }

    private Boolean isAutoLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        var autoLoginAttribute = session.getAttribute(AUTO_LOGIN_PARAM);
        session.removeAttribute(AUTO_LOGIN_PARAM);
        logger.info(autoLoginAttribute.equals(true));
        return autoLoginAttribute.equals(true);
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, Boolean isAutoLogin) {
        // Cookie refreshTokenCookie = new Cookie("X-Refresh-Token", refreshToken);
        // refreshTokenCookie.setHttpOnly(true);
        // refreshTokenCookie.setSecure(true);
        // refreshTokenCookie.setPath("/");
        // if(isAutoLogin) {
        //     refreshTokenCookie.setMaxAge(refreshDay * 24 * 60 * 60);
        // }

        StringBuilder cookieHeader = new StringBuilder();
        cookieHeader.append("X-Refresh-Token=").append(refreshToken)
            .append("; HttpOnly")
            .append("; Secure")
            .append("; Path=/")
            .append("; SameSite=None");
        if(isAutoLogin) {
            cookieHeader.append("; Max-Age=").append(refreshDay * 24 * 60 * 60);
        }

        response.addHeader("Set-Cookie", cookieHeader.toString());
    }
}