package com.swyp.catsgotogedog.global;

import com.swyp.catsgotogedog.global.exception.ErrorCode;

public record CatsgotogedogApiResponse<T>(int status, String message, T data) {
	private static final int SUCCESS = 200;

	public static <T> CatsgotogedogApiResponse<T> success(String message, T data) {
		return new CatsgotogedogApiResponse<>(SUCCESS, message, data);
	}

	public static <T> CatsgotogedogApiResponse<T> fail(ErrorCode errorCode) {
		return new CatsgotogedogApiResponse<>(errorCode.getCode(), errorCode.getMessage(), null);
	}

}
