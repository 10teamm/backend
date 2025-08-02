package com.swyp.catsgotogedog.review.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.swyp.catsgotogedog.global.CatsgotogedogApiResponse;
import com.swyp.catsgotogedog.review.domain.request.CreateReviewRequest;
import com.swyp.catsgotogedog.review.domain.response.ReviewResponse;
import com.swyp.catsgotogedog.review.service.ReviewService;

import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
@Slf4j
public class ReviewController implements ReviewControllerSwagger {

	private final ReviewService reviewService;

	// 리뷰 작성
	@Override
	@PostMapping(value = "/{contentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CatsgotogedogApiResponse<?>> createReview(
		@PathVariable int contentId,
		@AuthenticationPrincipal String userId,
		@Valid @ModelAttribute @ParameterObject CreateReviewRequest createReviewRequest,
		@RequestParam(value = "images", required = false)List<MultipartFile> images) throws IOException {

		reviewService.createReview(contentId, userId, createReviewRequest, images);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(CatsgotogedogApiResponse.success("리뷰 생성 성공", null));
	}

	// 리뷰 수정
	@Override
	@PutMapping(value = "/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CatsgotogedogApiResponse<?>> updateReview(
		@PathVariable int reviewId,
		@AuthenticationPrincipal String userId,
		@Valid @ModelAttribute @ParameterObject CreateReviewRequest createReviewRequest,
		@RequestParam(value = "images", required = false)List<MultipartFile> images) {

		reviewService.updateReview(reviewId, userId, createReviewRequest, images);

		return ResponseEntity.ok(
			CatsgotogedogApiResponse.success("리뷰 수정 성공", null)
		);
	}

	// 리뷰 삭제
	@Override
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<CatsgotogedogApiResponse<?>> deleteReview(
		@PathVariable int reviewId,
		@AuthenticationPrincipal String userId) {

		reviewService.deleteReview(reviewId, userId);

		return ResponseEntity.ok(
			CatsgotogedogApiResponse.success("리뷰 삭제 성공", null)
		);
	}

	// 리뷰 이미지 삭제
	@Override
	@DeleteMapping("/{reviewId}/image/{imageId}")
	public ResponseEntity<CatsgotogedogApiResponse<?>> deleteReviewImage(
		@PathVariable(name = "reviewId") int reviewId,
		@PathVariable(name = "imageId") int imageId,
		@AuthenticationPrincipal String userId) {

		reviewService.deleteReviewImage(reviewId, imageId, userId);

		return ResponseEntity.ok(
			CatsgotogedogApiResponse.success("리뷰 이미지 삭제 성공", null)
		);
	}

	@Override
	@GetMapping("/content/{contentId}")
	public ResponseEntity<CatsgotogedogApiResponse<?>> fetchReviewsByContentId(
		@PathVariable int contentId,
		@RequestParam(defaultValue = "r") String sort) {

		List<ReviewResponse> reviewResponses = reviewService.fetchReviewsByContentId(contentId, sort);

		return ResponseEntity.ok(
			CatsgotogedogApiResponse.success("리뷰 조회 성공", reviewResponses)
		);
	}
}
