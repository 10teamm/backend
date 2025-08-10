package com.swyp.catsgotogedog.global.exception;

import org.springframework.http.HttpStatus;

import com.google.api.Http;

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
        REVIEW_FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN.value(), "리뷰 접근 권한이 없습니다."),

        // 404 Notfound
        MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 회원입니다."),
        CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 컨텐츠 게시글입니다."),
        REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 리뷰입니다."),
        RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "리소스를 찾을 수 없습니다."),
        REVIEW_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 리뷰 이미지입니다."),
        SIGUNGU_CODE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 지역을 찾을 수 없습니다."),
        SIDO_CODE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 시/도를 찾을 수 없습니다."),
        ENDPOINT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "요청한 API 엔드포인트를 찾을 수 없습니다."),

        // 405 Method not allowed
        METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "허용되지 않은 HTTP 메소드입니다."),

        // 400 Bad Request
        MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST.value(), "요청 본문 형식이 올바르지 않습니다."),
        ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST.value(), "유효성 검사에 실패했습니다."),
        MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST.value(), "필수 파라미터가 누락되었습니다."),
        SIGUNGU_NEEDS_WITH_SIDO_CODE(HttpStatus.BAD_REQUEST.value(), "시/군/구  코드는 반드시 시/도 코드와 함께 요청해야 합니다."),
        REPORT_REASON_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 신고 사유입니다."),
        ALREADY_REPORTED(HttpStatus.BAD_REQUEST.value(), "이미 신고한 리뷰입니다."),
        OWN_REVIEW_CANT_REPORT(HttpStatus.BAD_REQUEST.value(), "자신이 작성한 리뷰는 신고할 수 없어요."),
        CLOVA_HASHTAG_CLIENT_ERROR(HttpStatus.BAD_REQUEST.value(), "Hashtag 생성 요청중 오류 발생"),
        CLOVA_HASHTAG_SERVER_ERROR(HttpStatus.BAD_REQUEST.value(), "클로바 서버 연결 오류"),
        ALREADY_RECOMMENDED(HttpStatus.BAD_REQUEST.value(), "이미 좋아요된 리뷰입니다."),
        NOT_RECOMMENDED_REVIEW(HttpStatus.BAD_REQUEST.value(), "좋아요 상태인 리뷰가 아닙니다."),
        MISSING_REQUEST_HEADER(HttpStatus.BAD_REQUEST.value(), "필수 요청 헤더가 누락되었습니다."),
        CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST.value(), "입력값이 제약 조건을 위반했습니다."),

        // 408 - 요청 타임아웃
        REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT.value(), "요청 처리 시간이 초과되었습니다."),

        // 415 Unsupported Mediatype
        MEDIA_TYPE_NOT_SUPPORTED(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "지원하지 않는 미디어 타입(Content-type) 입니다."),

        // 413 - 페이로드 너무 큼
        PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE.value(), "요청 데이터 크기가 허용 범위를 초과했습니다."),

        // 429 - 요청 제한 초과
        TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS.value(), "요청 한도를 초과했습니다. 잠시 후 다시 시도해주세요."),

        // 503 - 서비스 이용 불가
        SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE.value(), "일시적으로 서비스를 이용할 수 없습니다."),

        // 502 - 외부 API 호출 실패
        EXTERNAL_API_ERROR(HttpStatus.BAD_GATEWAY.value(), "외부 서비스 호출에 실패했습니다."),

        // 504 - 게이트웨이 타임아웃
        GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT.value(), "외부 서비스 응답 시간이 초과되었습니다."),

        // 500 Internal Server Error
        INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "현재 서버가 원할하지 않습니다. 잠시 후 다시 시도 또는 개발자에게 문의 해주세요"),
        DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 처리 중 오류가 발생했습니다."),
        TRANSACTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "트랜잭션 처리 중 오류가 발생했습니다."),
        FILE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "파일 처리 중 오류가 발생했습니다."),
        
        // User 관련
        DUPLICATE_DISPLAY_NAME(HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 닉네임입니다."),
        DISPLAY_NAME_UPDATE_TOO_SOON(HttpStatus.BAD_REQUEST.value(), "닉네임은 24시간마다 한 번만 변경할 수 있습니다."),
        SAME_DISPLAY_NAME(HttpStatus.BAD_REQUEST.value(), "현재 닉네임과 동일합니다."),
        TOO_TOXIC_DISPLAY_NAME(HttpStatus.BAD_REQUEST.value(), "부적절한 닉네임입니다. 다른 닉네임을 사용해주세요."),

        // 반려동물 관련 (Pet)
        PET_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 반려동물입니다."),
        PET_SIZE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 반려동물 크기입니다."),
        INVALID_PET_DATA(HttpStatus.BAD_REQUEST.value(), "반려동물 데이터가 유효하지 않습니다."),
        INVALID_PET_GENDER(HttpStatus.BAD_REQUEST.value(), "반려동물 성별은 M(수컷) 또는 F(암컷)이어야 합니다."),
        PET_NAME_REQUIRED(HttpStatus.BAD_REQUEST.value(), "반려동물 이름은 필수입니다."),
        PET_BIRTH_REQUIRED(HttpStatus.BAD_REQUEST.value(), "반려동물 생년월일은 필수입니다."),
        PET_SIZE_REQUIRED(HttpStatus.BAD_REQUEST.value(), "반려동물 크기는 필수입니다."),
        PET_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST.value(), "반려동물은 최대 10마리까지만 등록할 수 있습니다."),

        // Image Validator Error
        INVALID_IMAGE_NAME(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 이미지 이름입니다."),
        INVALID_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST.value(), "지원하지 않는 이미지 확장자입니다."),
        INVALID_IMAGE_FORMAT(HttpStatus.BAD_REQUEST.value(), "지원하지 않는 이미지 형식입니다."),
        IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "이미지 파일이 누락 되었습니다."),
        IMAGE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST.value(), "이미지 크기가 허용 범위를 초과했습니다."),
        IMAGE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST.value(), "최대 이미지 업로드 개수를 초과했습니다."),
        IMAGE_VALIDATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미지 유효성 검사에 실패했습니다."),
        REVIEW_IMAGE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST.value(), "리뷰 이미지는 최대 3개까지 업로드 가능합니다."),

        // Image Storage Error
        IMAGE_KEY_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "이미지 키가 누락 되었습니다."),
        IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미지 업로드에 실패했습니다."),

        // 필터링 API 관련
        PERSPECTIVE_API_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Perspective API 연결에 실패했습니다."),
        PERSPECTIVE_API_RESPONSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Perspective API 응답 처리에 실패했습니다."),
        PERSPECTIVE_API_REQUEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Perspective API 요청 준비에 실패했습니다."),

        // Stream IO Exception
        STREAM_IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR.value(), "스트림 처리 중 오류가 발생했습니다.");

        private final int code;
        private final String message;
}