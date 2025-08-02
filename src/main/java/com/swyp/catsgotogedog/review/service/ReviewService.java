package com.swyp.catsgotogedog.review.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import com.swyp.catsgotogedog.common.util.image.storage.ImageStorageService;
import com.swyp.catsgotogedog.common.util.image.storage.dto.ImageInfo;
import com.swyp.catsgotogedog.common.util.image.validator.ImageUploadType;
import com.swyp.catsgotogedog.common.util.image.validator.ImageValidator;
import com.swyp.catsgotogedog.content.domain.entity.Content;
import com.swyp.catsgotogedog.content.repository.ContentRepository;
import com.swyp.catsgotogedog.global.exception.CatsgotogedogException;
import com.swyp.catsgotogedog.global.exception.ErrorCode;
import com.swyp.catsgotogedog.review.domain.entity.Review;
import com.swyp.catsgotogedog.review.domain.entity.ReviewImage;
import com.swyp.catsgotogedog.review.domain.request.CreateReviewRequest;
import com.swyp.catsgotogedog.review.repository.ReviewImageRepository;
import com.swyp.catsgotogedog.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewImageRepository reviewImageRepository;
	private final UserRepository userRepository;
	private final ContentRepository contentRepository;
	private final ImageStorageService imageStorageService;

	// 리뷰 작성
	@Transactional
	public void createReview(int contentId, String userId, CreateReviewRequest request, List<MultipartFile> images) {
		User user = validateDatas(userId);
		Content content = validateDatas(contentId);


		Review uploadedReview = reviewRepository.save(Review.builder()
			.userId(user.getUserId())
			.contentId(content.getContentId())
			.score(request.getScore())
			.content(request.getContent())
			.build());

		if(images != null && !images.isEmpty()) {
			if(images.size() > ImageUploadType.REVIEW.getMaxFiles()) {
				throw new CatsgotogedogException(ErrorCode.REVIEW_IMAGE_LIMIT_EXCEEDED);
			}
			uploadAndSaveReviewImages(uploadedReview, images);
		}

	}

	private User validateDatas(String userId) {
		return userRepository.findById(Integer.parseInt(userId))
			.orElseThrow(() -> new CatsgotogedogException(ErrorCode.MEMBER_NOT_FOUND));
	}

	private Content validateDatas(int contentId) {
		return contentRepository.findById(contentId)
			.orElseThrow(() -> new CatsgotogedogException(ErrorCode.CONTENT_NOT_FOUND));
	}

	private void uploadAndSaveReviewImages(Review review, List<MultipartFile> images) {
		if(images.size() > ImageUploadType.REVIEW.getMaxFiles()) {
			throw new CatsgotogedogException(ErrorCode.REVIEW_IMAGE_LIMIT_EXCEEDED);
		}

		List<ImageInfo> imageInfos = imageStorageService.upload(images, ImageUploadType.REVIEW);

		List<ReviewImage> saveImages = imageInfos.stream()
			.map(imageInfo -> ReviewImage.builder()
				.review(review)
				.imageFilename(imageInfo.key())
				.imageUrl(imageInfo.url())
				.build()
			).toList();

		reviewImageRepository.saveAll(saveImages);
	}
}
