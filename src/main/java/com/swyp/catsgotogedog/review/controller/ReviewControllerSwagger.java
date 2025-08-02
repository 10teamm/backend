package com.swyp.catsgotogedog.review.controller;

import java.security.Principal;
import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.swyp.catsgotogedog.global.CatsgotogedogApiResponse;
import com.swyp.catsgotogedog.review.domain.request.CreateReviewRequest;

import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Review", description = "리뷰 관련 API")
public interface ReviewControllerSwagger {

	@Operation(
		summary = "리뷰를 작성합니다.",
		description = "사용자 인증을 통해 리뷰를 작성합니다."
	)
	@SecurityRequirement(name = "bearer-key")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "리뷰 작성 성공"
			, content = @Content(schema = @Schema(implementation = CatsgotogedogApiResponse.class))),
		@ApiResponse(responseCode = "400", description = "요청 값이 누락되거나 유효하지 않음"
			, content = @Content(schema = @Schema(implementation = CatsgotogedogApiResponse.class))),
		@ApiResponse(responseCode = "401", description = "유효하지 않은 토큰"
			, content = @Content(schema = @Schema(implementation = CatsgotogedogApiResponse.class))),
		@ApiResponse(responseCode = "404", description = "ContentId가 존재하지 않음"
			, content = @Content(schema = @Schema(implementation = CatsgotogedogApiResponse.class)))
	})
	ResponseEntity<CatsgotogedogApiResponse<?>> createReview(
		@Parameter(description = "리뷰를 작성할 컨텐츠 ID", required = true)
		@PathVariable int contentId,

		@Parameter(hidden = true)
		@AuthenticationPrincipal String userId,

		@RequestPart(value = "createReviewRequest")
		@ModelAttribute @ParameterObject CreateReviewRequest createReviewRequest,

		@Parameter(description = "이미지 업로드 (최대 3장)")
		@RequestParam(value = "images") List<MultipartFile> images
	) throws IOException;

	@Operation(
		summary = "작성 리뷰를 수정합니다.",
		description = "사용자 인증을 통해 리뷰를 수정합니다."
	)
	@SecurityRequirement(name = "bearer-key")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "리뷰 수정 성공"
			, content = @Content(schema = @Schema(implementation = CatsgotogedogApiResponse.class))),
		@ApiResponse(responseCode = "400", description = "요청 값이 누락되거나 유효하지 않음"
			, content = @Content(schema = @Schema(implementation = CatsgotogedogApiResponse.class))),
		@ApiResponse(responseCode = "401", description = "유효하지 않은 토큰"
			, content = @Content(schema = @Schema(implementation = CatsgotogedogApiResponse.class))),
		@ApiResponse(responseCode = "404", description = "ContentId가 존재하지 않음"
			, content = @Content(schema = @Schema(implementation = CatsgotogedogApiResponse.class)))
	})
	ResponseEntity<CatsgotogedogApiResponse<?>> updateReview(
		@Parameter(description = "수정할 리뷰 ID", required = true)
		@PathVariable int reviewId,

		@Parameter(hidden = true)
		@AuthenticationPrincipal String userId,

		@RequestPart(value = "createReviewRequest")
		@ModelAttribute @ParameterObject CreateReviewRequest createReviewRequest,

		@Parameter(description = "이미지 업로드 (최대 3장)")
		@RequestParam(value = "images") List<MultipartFile> images
	);
}
