package com.swyp.catsgotogedog.global.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swyp.catsgotogedog.global.CatsgotogedogApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CatsgotogedogException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleCatsgotogedogException(CatsgotogedogException ex) {
		log.error("CatsgotogedogException : {}", ex.getMessage());
		return createErrorResponse(ex.getErrorCode());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleException(Exception ex) {
		log.error("Exception : {}", ex.getMessage());
		int errorCode = ErrorCode.INTERNAL_SERVER_ERROR.getCode();
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(errorCode));
	}

	private ResponseEntity<CatsgotogedogApiResponse<Object>> createErrorResponse(ErrorCode errorCode) {
		int errorCodeValue = errorCode.getCode();
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(errorCode);
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(errorCodeValue));
	}
}
