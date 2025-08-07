package com.swyp.catsgotogedog.common.security.filter;

import com.swyp.catsgotogedog.common.util.JwtTokenUtil;
import com.swyp.catsgotogedog.global.exception.CatsgotogedogException;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter implements Filter {

    private final JwtTokenUtil jwt;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String bearer = request.getHeader("Authorization");

        if (bearer != null && bearer.startsWith("Bearer ")) {
            try {
                String token = bearer.substring(7);
                String sub   = jwt.getSubject(token);

                var auth = new UsernamePasswordAuthenticationToken(
                    sub, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (MalformedJwtException e) {
                throw new MalformedJwtException("잘못된 토큰 형식입니다. 요청된 Authorization : " + bearer, e);
            }
        }
        chain.doFilter(req, res);
    }
}