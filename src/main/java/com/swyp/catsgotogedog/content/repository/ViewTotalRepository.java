package com.swyp.catsgotogedog.content.repository;

import com.swyp.catsgotogedog.content.domain.entity.ViewTotal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewTotalRepository extends JpaRepository<ViewTotal, Integer> {
    @Query(value = """
        INSERT INTO view_total (content_id, total_view, updated_at)
        VALUES (:contentId, 1, NOW())
        ON DUPLICATE KEY UPDATE
          total_view = total_view + 1,
          updated_at = NOW()
        """, nativeQuery = true)
    void upsertAndIncrease(int contentId);
}
