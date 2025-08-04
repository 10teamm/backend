package com.swyp.catsgotogedog.review.domain.response;

import java.util.List;

public record MyReviewPageResponse(
	List<MyReviewResponse> reviews,
	int totalElements,
	int totalPages,
	int currentPage,
	int size,
	boolean hasNext,
	boolean hasPrevious
) {}
