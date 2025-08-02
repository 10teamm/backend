package com.swyp.catsgotogedog.review.domain.response;

import java.math.BigDecimal;
import java.util.List;

public record ReviewResponse (
	int contentId,
	int reviewId,
	int userId,
	String displayName,
	String profileImageUrl,
	String content,
	BigDecimal score,
	int recommendedNumber,
	List<ReviewImageResponse> images
) {}
