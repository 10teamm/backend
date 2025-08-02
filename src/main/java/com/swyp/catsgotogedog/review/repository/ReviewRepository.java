package com.swyp.catsgotogedog.review.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.swyp.catsgotogedog.review.domain.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	/**
	 * reviewId를 이용해 reviewImage 컬렉션도 함께 조회
	 * @param reviewId
	 * @return Review Entity
	 */
	@Query("SELECT r FROM Review r LEFT JOIN FETCH r.reviewImages WHERE r.reviewId = :reviewId")
	Optional<Review> findByIdWithImages(@Param("reviewId") int reviewId);
}
