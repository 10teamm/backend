package com.swyp.catsgotogedog.review.domain.response;

import java.util.List;

public record ContentReviewPageResponse (
	List<ReviewResponse> reviews,
	List<ReviewImageResponse> reviewImages,
	int totalElements,
	int totalPages,
	int currentPage,
	int size,
	boolean hasNext,
	boolean hasPrevious
) {
}
