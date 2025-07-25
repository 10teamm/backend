package com.swyp.catsgotogedog.User.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import com.swyp.catsgotogedog.User.domain.response.AccessTokenResponse;
import com.swyp.catsgotogedog.global.CatsgotogedogApiResponse;

@Tag(name = "User", description = "사용자 관련 API")
public interface UserControllerSwagger {

    @Operation(
        summary = "액세스 토큰 재발급",
        description = "리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급받습니다.\n"
			+ "재발급된 토큰은 body를 통해 반환됩니다."
            + " Cookie를 통해 Refresh-Token값을 읽어 재발급을 진행합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"
                    , content = @Content(schema = @Schema(implementation = AccessTokenResponse.class))),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰"
                    , content = @Content(schema = @Schema(implementation = CatsgotogedogApiResponse.class)))
    })
    ResponseEntity<?> reIssue(
        @Parameter(description = "리프레시 토큰", hidden = true)
        String refresh
    );

    @Operation(
        summary = "로그아웃",
        description = "사용자 로그아웃을 처리하고 리프레시 토큰을 제거합니다. Cookie를 통해 Refresh-Token값을 읽어 로그아웃 처리를 진행합니다."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses({
		@ApiResponse(responseCode = "200", description = "로그아웃 성공"
                    , content = @Content(schema = @Schema(implementation = CatsgotogedogApiResponse.class))),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰"
                    , content = @Content(schema = @Schema(implementation = CatsgotogedogApiResponse.class)))
    })
    ResponseEntity<CatsgotogedogApiResponse<?>> logout(
        @Parameter(description = "리프레시 토큰", hidden = true)
        String refresh
    );
}