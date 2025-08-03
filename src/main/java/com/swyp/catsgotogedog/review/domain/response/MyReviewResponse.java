package com.swyp.catsgotogedog.review.domain.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record MyReviewResponse(
	int contentId,
	String contentTitle,
	int reviewId,
	String content,
	BigDecimal score,
	int recommendedNumber,
	LocalDateTime createdAt,
	List<ReviewImageResponse> images
) {}
