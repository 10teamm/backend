package com.swyp.catsgotogedog.User.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "사용자 관리", description = "사용자 관련 API")
public interface UserControllerSwagger {

    @Operation(
        summary = "토큰 재발급",
        description = "리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급받습니다.\n"
			+ "재발급 토큰은 Header를 통해 반환됩니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰 또는 사용자를 찾을 수 없음")
    })
    ResponseEntity<Void> reissue(
        @Parameter(description = "리프레시 토큰", required = true)
        String refresh
    );

    @Operation(
        summary = "로그아웃",
        description = "사용자 로그아웃을 처리하고 리프레시 토큰을 제거합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    ResponseEntity<?> logout(
        @Parameter(description = "리프레시 토큰", required = true)
        String refresh
    );
}