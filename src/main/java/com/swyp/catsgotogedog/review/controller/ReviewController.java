package com.swyp.catsgotogedog.review.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.swyp.catsgotogedog.global.CatsgotogedogApiResponse;
import com.swyp.catsgotogedog.review.domain.request.CreateReviewRequest;
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

	@Override
	@PostMapping(value = "/{contentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CatsgotogedogApiResponse<?>> createReview(
		@PathVariable int contentId,
		@AuthenticationPrincipal String userId,
		@Valid @ModelAttribute @ParameterObject CreateReviewRequest createReviewRequest,
		@RequestParam(value = "images", required = false)List<MultipartFile> images) throws IOException {

		reviewService.createReview(contentId, userId, createReviewRequest, images);

		return ResponseEntity.ok(
			CatsgotogedogApiResponse.success("리뷰 생성 성공", null)
		);
	}
}
