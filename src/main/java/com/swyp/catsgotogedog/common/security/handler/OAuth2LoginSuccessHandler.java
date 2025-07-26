package com.swyp.catsgotogedog.common.security.handler;

import static com.swyp.catsgotogedog.common.security.filter.OAuth2AutoLoginFilter.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import com.swyp.catsgotogedog.User.service.RefreshTokenService;
import com.swyp.catsgotogedog.common.security.service.PrincipalDetails;
import com.swyp.catsgotogedog.common.util.JwtTokenUtil;
import com.swyp.catsgotogedog.global.exception.CatsgotogedogException;
import com.swyp.catsgotogedog.global.exception.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
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
                .orElseThrow(() -> new CatsgotogedogException(ErrorCode.MEMBER_NOT_FOUND));

        //String access  = jwt.createAccessToken(String.valueOf(user.getUserId()), user.getEmail());
        String refresh = jwt.createRefreshToken(String.valueOf(user.getUserId()), user.getEmail(), user.getDisplayName());

        rtService.save(user, refresh, jwt.getRefreshTokenExpiry());

        addRefreshTokenCookie(response, refresh, isAutoLogin(request));

        String targetUrl;
        String refererHeader = request.getHeader("Referer");
        log.info("refererHeader :: {} ", refererHeader);
        URL parsedUrl = null;

        // Referer 헤더를 통해 Redirect
        if (refererHeader != null && !refererHeader.isEmpty()) {
            try {
                parsedUrl = new URL(refererHeader);
                if (parsedUrl.getHost() != null && !parsedUrl.getHost().isEmpty()) {
                    UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                        .scheme(parsedUrl.getProtocol())
                        .host(parsedUrl.getHost());
                    if (parsedUrl.getPort() != -1) {
                        builder.port(parsedUrl.getPort());
                    }
                    targetUrl = builder.path("/authredirect").build().toUriString();
                    log.info("Referer 헤더를 통한 Redirect :: {}", targetUrl);
                    getRedirectStrategy().sendRedirect(request, response, targetUrl);
                    return;
                }
            } catch (MalformedURLException e) {
                log.warn("잘못된 형식의 URL :: {}", refererHeader, e);
            }
        }

        String requestURLString = request.getRequestURL().toString();
        try {
            URL requestURL = new URL(requestURLString);
            String host = requestURL.getHost();
            String scheme = requestURL.getProtocol();
            int port = requestURL.getPort();

            if (host != null && (host.equals("localhost") || host.equals("127.0.0.1"))) {
                if (frontend_base_url != null && !frontend_base_url.isEmpty()) {
                    // Use the specifically configured localhost frontend URL
                    targetUrl = UriComponentsBuilder.fromUriString(frontend_base_url)
                        .path("/authredirect")
                        .build()
                        .toUriString();
                    log.info("Referer Header가 존재하지 않아 frontend_base_url로 redirect :: {}", targetUrl);
                    getRedirectStrategy().sendRedirect(request, response, targetUrl);
                } else {
                    UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                        .scheme(scheme)
                        .host(host);
                    if (port != -1) {
                        builder.port(port);
                    }
                    targetUrl = builder.path("/authredirect").build().toUriString();
                    log.info("frontend_base_url이 존재하지 않아 request url로 강제 redirect :: {}", targetUrl);
                    getRedirectStrategy().sendRedirect(request, response, targetUrl);
                }
            }
        } catch (MalformedURLException e) {
            log.warn("잘못된 형식의 비 Referer URL :: {}", requestURLString, e);
        }
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