package com.swyp.catsgotogedog.global.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.transaction.TransactionException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.swyp.catsgotogedog.global.CatsgotogedogApiResponse;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CatsgotogedogException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleCatsgotogedogException(CatsgotogedogException ex) {
		log.error("CatsgotogedogException : {}", ex.getMessage(), ex);
		return createErrorResponse(ex.getErrorCode());
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		log.error("CatsgotogedogException: {}", e.getMessage(), e);
		int errorCode = ErrorCode.IMAGE_SIZE_EXCEEDED.getCode();
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(ErrorCode.IMAGE_SIZE_EXCEEDED);
		return ResponseEntity
				.status(errorCode)
				.body(response);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		log.error("MethodArgumentTypeMismatchException: {}", e.getMessage(), e);
		String errorMessage = String.format("파라미터 '%s'의 타입이 올바르지 않습니다. 요청된 타입: %s",
			e.getName(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "알 수 없음");
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(HttpStatus.BAD_REQUEST.value(), errorMessage);
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("MethodArgumentNotValidException: {}", e.getMessage(), e);
		BindingResult bindingResult = e.getBindingResult();
		Map<String, String> errors = new HashMap<>();
		bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(ErrorCode.ARGUMENT_NOT_VALID, errors);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
		log.error("MissingServletRequestParameterException: {}", e.getMessage(), e);
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(ErrorCode.MISSING_REQUEST_PARAMETER, e.getParameterName());
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.error("HttpMessageNotReadableException: {}", e.getMessage(), e);
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(ErrorCode.MESSAGE_NOT_READABLE);
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException  e) {
		log.error("HttpRequestMethodNotSupportedException: {}", e.getMessage(), e);
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(ErrorCode.METHOD_NOT_ALLOWED, e.getMethod());
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}

	@ExceptionHandler(UnAuthorizedAccessException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleUnAuthorizedAccessException(UnAuthorizedAccessException  e) {
		log.error("UnAuthorizedAccessException: {}", e.getMessage(), e);
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(ErrorCode.UNAUTHORIZED_ACCESS);
		return ResponseEntity
			.status(HttpStatus.UNAUTHORIZED)
			.body(response);
	}

	@ExceptionHandler(ExpiredTokenException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleExpiredTokenException(ExpiredTokenException e) {
		log.warn("ExpiredTokenException : {}", e.getMessage(), e);
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(ErrorCode.EXPIRED_TOKEN);
		return ResponseEntity
			.status(HttpStatus.UNAUTHORIZED)
			.body(response);
	}

	@ExceptionHandler(MalformedJwtException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleMalformedJwtException(MalformedJwtException e) {
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(HttpStatus.BAD_REQUEST.value() , e.getMessage());
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
		log.error("HttpMediaTypeNotSupportedException: {}", e.getMessage(), e);
		String errorMessage = String.format("지원하지 않는 미디어 타입입니다. 요청된 타입: %s, 지원되는 타입: %s",
			e.getContentType(), e.getSupportedMediaTypes());
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(
			ErrorCode.MEDIA_TYPE_NOT_SUPPORTED.getCode(),
			errorMessage
		);
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleNoHandlerFoundException(NoHandlerFoundException e) {
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(ErrorCode.ENDPOINT_NOT_FOUND , ErrorCode.ENDPOINT_NOT_FOUND.getMessage());
		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(response);
	}

	@ExceptionHandler({JsonParseException.class, JsonMappingException.class})
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleJsonException(Exception e) {
		log.error("JSON Exception: {}", e.getMessage(), e);
		String detailedMessage = "JSON 형식이 올바르지 않습니다: " + e.getMessage();
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(
			ErrorCode.MESSAGE_NOT_READABLE.getCode(),
			detailedMessage
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
		log.error("MissingRequestHeaderException: {}", e.getMessage(), e);
		String errorMessage = String.format("필수 헤더 '%s'가 누락되었습니다.", e.getHeaderName());
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(
			ErrorCode.MISSING_REQUEST_HEADER.getCode(),
			errorMessage
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException e) {
		log.error("ConstraintViolationException: {}", e.getMessage(), e);
		Map<String, String> errors = new HashMap<>();
		e.getConstraintViolations().forEach(violation -> {
			String propertyPath = violation.getPropertyPath().toString();
			String message = violation.getMessage();
			errors.put(propertyPath, message);
		});

		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(ErrorCode.CONSTRAINT_VIOLATION, errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(RequestRejectedException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleRequestRejectedException(RequestRejectedException e) {
		log.error("RequestRejectedException: {}", e.getMessage(), e);
		return createErrorResponse(ErrorCode.TOO_MANY_REQUESTS);
	}

	@ExceptionHandler({ResourceAccessException.class, HttpServerErrorException.ServiceUnavailable.class})
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleServiceUnavailableException(Exception e) {
		log.error("ServiceUnavailableException: {}", e.getMessage(), e);
		return createErrorResponse(ErrorCode.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler({ConnectTimeoutException.class, SocketTimeoutException.class})
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleTimeoutException(Exception e) {
		log.error("TimeoutException: {}", e.getMessage(), e);
		return createErrorResponse(ErrorCode.REQUEST_TIMEOUT);
	}

	@ExceptionHandler({DataAccessException.class, SQLException.class})
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleDataAccessException(Exception e) {
		log.error("DataAccessException: {}", e.getMessage(), e);
		return createErrorResponse(ErrorCode.DATABASE_ERROR);
	}

	@ExceptionHandler(TransactionException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleTransactionException(TransactionException e) {
		log.error("TransactionException: {}", e.getMessage(), e);
		return createErrorResponse(ErrorCode.TRANSACTION_ERROR);
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleAccessDeniedException(AccessDeniedException e) {
		log.error("AccessDeniedException: {}", e.getMessage(), e);
		return createErrorResponse(ErrorCode.FORBIDDEN_ACCESS);
	}

	@ExceptionHandler({IOException.class, FileNotFoundException.class})
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleFileException(Exception e) {
		log.error("FileException: {}", e.getMessage(), e);
		return createErrorResponse(ErrorCode.FILE_PROCESSING_ERROR);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<CatsgotogedogApiResponse<Object>> handleException(Exception ex) {
		log.error("예상하지 못한 오류 발생 : {}", ex.getMessage(), ex);
		int errorCode = ErrorCode.INTERNAL_SERVER_ERROR.getCode();
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
		return ResponseEntity
			.status(errorCode)
			.body(response);
	}

	private ResponseEntity<CatsgotogedogApiResponse<Object>> createErrorResponse(ErrorCode errorCode) {
		int errorCodeValue = errorCode.getCode();
		CatsgotogedogApiResponse<Object> response = CatsgotogedogApiResponse.fail(errorCode);
		return ResponseEntity
			.status(errorCodeValue)
			.body(response);
	}

}
