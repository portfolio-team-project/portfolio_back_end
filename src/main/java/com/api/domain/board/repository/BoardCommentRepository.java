package com.api.domain.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.domain.board.entity.BoardCommentEntity;
import com.api.domain.board.entity.BoardEntity;

public interface BoardCommentRepository extends JpaRepository<BoardCommentEntity, Long> {

    List<BoardCommentEntity> findByBoard_LocalIdAndDelYnOrderByCreatedDateAscLocalIdAsc(Long boardId, String delYn);
    List<BoardCommentEntity> findByBoardAndDelYn(BoardEntity board, String delYn);
}
