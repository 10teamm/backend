package com.swyp.catsgotogedog.content.repository;

import com.swyp.catsgotogedog.content.domain.entity.ViewTotal;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ViewTotalRepository extends JpaRepository<ViewTotal, Integer> {
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO view_total (content_id, total_view, updated_at)
        VALUES (:contentId, 1, NOW())
        ON DUPLICATE KEY UPDATE
          total_view = total_view + 1,
          updated_at = NOW()
        """, nativeQuery = true)
    void upsertAndIncrease(int contentId);

    @Query("""
        SELECT vt.totalView
          FROM ViewTotal vt
         WHERE vt.contentId = :contentId
    """)
    int findTotalViewByContentId(int contentId);
}
