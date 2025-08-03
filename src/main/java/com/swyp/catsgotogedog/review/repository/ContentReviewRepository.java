package com.swyp.catsgotogedog.review.repository;

import com.swyp.catsgotogedog.review.domain.entity.ContentReview;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


public interface ContentReviewRepository extends JpaRepository<ContentReview, Integer> {
    ContentReview findByContentId(int contentId);

    @Query("select avg(cr.score) from ContentReview cr where cr.contentId = :contentId")
    Double findAvgScoreByContentId(@Param("contentId") int contentId);
}
