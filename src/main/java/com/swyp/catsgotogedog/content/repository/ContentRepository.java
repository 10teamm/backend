package com.swyp.catsgotogedog.content.repository;

import java.util.List;

import com.swyp.catsgotogedog.content.domain.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.swyp.catsgotogedog.content.domain.entity.Content;

public interface ContentRepository extends JpaRepository<Content, Integer> {
    Content findByContentId(int contentId);

    @Query(value =
        "SELECT c.* FROM content c "
        + "LEFT JOIN hashtag h ON c.content_id = h.content_id "
        + "WHERE h.content_id IS NULL", nativeQuery = true)
    List<Content> findContentsWithoutHashtags();

    List<Content> findAllByContentIdIn(List<Integer> contentIds);

    /**
     * 이미지가 있는 컨텐츠만 랜덤으로 5개 조회 (AI 추천용)
     */
    @Query(value = """
    SELECT DISTINCT c.* FROM content c 
    WHERE c.image != ""
    AND c.overview != ""
    ORDER BY RAND() LIMIT 5
    """, nativeQuery = true)
    List<Content> findRandomContentsWithImages();
}
