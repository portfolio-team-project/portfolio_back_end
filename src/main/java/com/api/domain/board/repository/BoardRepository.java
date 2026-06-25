package com.api.domain.board.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.domain.board.entity.BoardEntity;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    @Query("SELECT b FROM BoardEntity b WHERE b.noticeYn = :noticeYn AND b.delYn = :delYn " +
           "ORDER BY CASE WHEN b.updatedDate IS NOT NULL THEN b.updatedDate ELSE b.createdDate END DESC, b.localId DESC")
    List<BoardEntity> findTop2NoticesByLatestDate(@Param("noticeYn") String noticeYn, @Param("delYn") String delYn, Pageable pageable);

    Page<BoardEntity> findByNoticeYnAndDelYn(String noticeYn, String delYn, Pageable pageable);
    Page<BoardEntity> findByNoticeYnAndDelYnAndTitleContaining(String noticeYn, String delYn, String title, Pageable pageable);
}
