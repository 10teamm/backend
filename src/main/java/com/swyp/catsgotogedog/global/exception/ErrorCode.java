package com.swyp.catsgotogedog.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 401 BadRequest
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "만료된 토큰입니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED.value(), "인증되지 않은 접근입니다."),

    // 403 Forbidden
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN.value(), "접근 권한이 없습니다."),

    // 404 Notfound
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 회원입니다."),
    CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 컨텐츠 게시글입니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 리뷰입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "리소스를 찾을 수 없습니다."),

    // 405 Method not allowed
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "허용되지 않은 HTTP 메소드입니다."),

    // 400 Bad Request
    MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST.value(), "요청 본문 형식이 올바르지 않습니다."),
    ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST.value(), "유효성 검사에 실패했습니다."),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST.value(), "필수 파라미터가 누락되었습니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다."),

    // Image Storage Errors
    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미지 업로드에 실패했습니다.");

    private final int code;
    private final String message;
}