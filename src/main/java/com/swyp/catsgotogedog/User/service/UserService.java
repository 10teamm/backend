package com.swyp.catsgotogedog.User.service;

import org.springframework.stereotype.Service;

import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import com.swyp.catsgotogedog.common.util.JwtTokenUtil;
import com.swyp.catsgotogedog.global.exception.ErrorCode;
import com.swyp.catsgotogedog.global.exception.InvalidTokenException;
import com.swyp.catsgotogedog.global.exception.UnAuthorizedAccessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final RefreshTokenService rtService;
	private final JwtTokenUtil jwt;

	public String reIssue(String refreshToken) {

		if(!rtService.validate(refreshToken)) {
			throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
		}

		int userId = Integer.parseInt(jwt.getSubject(refreshToken));
		String email = jwt.getEmail(refreshToken);
		String displayName = jwt.getDisplayName(refreshToken);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UnAuthorizedAccessException(ErrorCode.UNAUTHORIZED_ACCESS));

		return jwt.createAccessToken(String.valueOf(userId), email, displayName);
	}

	public void logout(String refreshToken) {
		if (!rtService.validate(refreshToken)) {
			throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
		}
		rtService.delete(refreshToken);
	}
}
