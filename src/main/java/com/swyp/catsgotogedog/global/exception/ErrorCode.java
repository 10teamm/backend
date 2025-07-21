package com.swyp.catsgotogedog.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// 401 BadRequest
	INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
	EXPIRED_TOKEN(401, "만료된 토큰입니다."),
	UNAUTHORIZED_ACCESS(401, "인증되지 않은 접근입니다."),

	// 403 Forbidden
	FORBIDDEN_ACCESS(403, "접근 권한이 없습니다."),

	// 404 Notfound
	MEMBER_NOT_FOUND(404, "존재하지 않는 회원입니다."),
	CONTENT_NOT_FOUND(404, "존재하지 않는 컨텐츠 게시글입니다."),
	REVIEW_NOT_FOUND(404, "존재하지 않는 리뷰입니다."),
	RESOURCE_NOT_FOUND(404, "리소스를 찾을 수 없습니다."),

	// 500 Internal Server Error
	INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다.");

	private final int code;
	private final String message;
}
