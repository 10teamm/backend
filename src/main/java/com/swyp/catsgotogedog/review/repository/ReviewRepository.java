package com.swyp.catsgotogedog.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

	// 페이징 컨텐츠 리뷰 목록 조회
	@Query("SELECT DISTINCT r FROM Review r "
		+ "LEFT JOIN FETCH r.reviewImages "
		+ "WHERE r.contentEntity.contentId = :contentId")
	Page<Review> findByContentIdWithUserAndReviewImages(
		@Param("contentId") int contentId,
		Pageable pageable);

	@EntityGraph(attributePaths = {"reviewImages"})
	Page<Review> findByUserId(int userId, Pageable pageable);

	// 페이징 자신이 작성한 리뷰 목록 조회
	@Query(value = "SELECT DISTINCT r FROM Review r "
		+ "LEFT JOIN FETCH r.contentEntity c "
		+ "LEFT JOIN FETCH r.reviewImages "
		+ "WHERE r.userId = :userId",
	countQuery = "SELECT COUNT(r) FROM Review r WHERE r.userId = :userId")
	Page<Review> findByUserIdWithContent(@Param("userId") int userId, Pageable pageable);
}
