package com.swyp.catsgotogedog.common.security.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class OAuth2AutoLoginFilter extends OncePerRequestFilter {

	public final static String AUTO_LOGIN_PARAM = "autoLogin";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		if(request.getRequestURI().contains("/oauth2/authorization/")) {
			String autoLoginParam = request.getParameter(AUTO_LOGIN_PARAM);

			if(autoLoginParam == null) {
				autoLoginParam = "false";
			}

			boolean autoLogin = "true".equalsIgnoreCase(autoLoginParam);

			HttpSession session = request.getSession(true);
			session.setAttribute(AUTO_LOGIN_PARAM, autoLogin);
		}

		filterChain.doFilter(request, response);
	}
}
