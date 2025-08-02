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
		User user = validateUser(userId);
		Content content = validateContent(contentId);


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

	// 리뷰 수정
	@Transactional
	public void updateReview(int reviewId, String userId, CreateReviewRequest request, List<MultipartFile> images) {
		User user = validateUser(userId);
		Review review = reviewRepository.findByIdAndUserId(reviewId, userId)
			.orElseThrow(() -> new CatsgotogedogException(ErrorCode.REVIEW_NOT_FOUND));

		review.setScore(request.getScore());
		review.setContent(request.getContent());

		if(images != null && !images.isEmpty()) {
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

	// 리뷰 삭제
	@Transactional
	public void deleteReview(int reviewId, String userId) {
		User user = validateUser(userId);
		validateReview(reviewId);
		Review review = reviewRepository.findByIdAndUserId(reviewId, userId)
			.orElseThrow(() -> new CatsgotogedogException(ErrorCode.FORBIDDEN_ACCESS));

		List<ReviewImage> images = reviewImageRepository.findByReview(review);

		images.forEach(image -> imageStorageService.delete(image.getImageFilename()));

		reviewRepository.delete(review);

	}

	// 리뷰 이미지 삭제
	@Transactional
	public void deleteReviewImage(int reviewId, int imageId, String userId) {
		validateUser(userId);
		validateReview(reviewId);

		reviewRepository.findByIdAndUserId(reviewId, userId)
				.orElseThrow(() -> new CatsgotogedogException(ErrorCode.FORBIDDEN_ACCESS));

		ReviewImage image = reviewImageRepository.findById(imageId)
				.orElseThrow(() -> new CatsgotogedogException(ErrorCode.REVIEW_IMAGE_NOT_FOUND));

		imageStorageService.delete(image.getImageFilename());
		reviewImageRepository.deleteById(imageId);
	}


	private User validateUser(String userId) {
		return userRepository.findById(Integer.parseInt(userId))
			.orElseThrow(() -> new CatsgotogedogException(ErrorCode.MEMBER_NOT_FOUND));
	}

	private Content validateContent(int contentId) {
		return contentRepository.findById(contentId)
			.orElseThrow(() -> new CatsgotogedogException(ErrorCode.CONTENT_NOT_FOUND));
	}

	private Review validateReview(int reviewId) {
		return reviewRepository.findById(reviewId)
			.orElseThrow(() -> new CatsgotogedogException(ErrorCode.REVIEW_NOT_FOUND));
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
