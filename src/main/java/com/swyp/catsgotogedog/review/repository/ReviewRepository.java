package com.swyp.catsgotogedog.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.swyp.catsgotogedog.review.domain.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	/**
	 * reviewId를 이용해 reviewImage 컬렉션도 함께 조회
	 * @param reviewId
	 * @return Optional<Review>
	 */
	@Query("SELECT r FROM Review r LEFT JOIN FETCH r.reviewImages WHERE r.reviewId = :reviewId")
	Optional<Review> findByIdWithImages(@Param("reviewId") int reviewId);

	/**
	 * reviewId와 userId 를 통한 리뷰 컬렉션 조회
	 * @param reviewId
	 * @param userId
	 * @return Optional<Review>
	 */
	@Query("SELECT r FROM Review r WHERE r.reviewId = :reviewId AND r.userId = :userId")
	Optional<Review> findByIdAndUserId(@Param("reviewId") int reviewId, String userId);

	List<Review> findByContentId(int contentId);

	@Query("SELECT DISTINCT r FROM Review r "
		+ "LEFT JOIN FETCH r.reviewImages "
		+ "WHERE r.contentId = :contentId "
		+ "ORDER BY "
		+ "CASE WHEN :sort = 'r' THEN r.recommendedNumber END DESC, "
		+ "CASE WHEN :sort = 'c' THEN r.createdAt END DESC, "
		+ "r.recommendedNumber DESC")
	List<Review> findByContentIdWithUserAndReviewImages(
		@Param("contentId") int contentId,
		@Param("sort") String sort);
}
