package com.api.domain.board.service;

import org.springframework.data.domain.Pageable;

import com.api.domain.board.dto.BoardDeleteRequest;
import com.api.domain.board.dto.BoardDetailResponse;
import com.api.domain.board.dto.BoardPageResponse;
import com.api.domain.board.dto.BoardRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface BoardService {
    void createBoard(BoardRequest boardRequest, String uuid, boolean isAdmin, HttpServletRequest request);
    BoardPageResponse getBoardList(Pageable pageable, String title);
    BoardDetailResponse getBoardDetail(Long localId);
    void updateBoard(Long localId, BoardRequest boardRequest, String uuid);
    void deleteBoard(Long localId, BoardDeleteRequest boardDeleteRequest, String uuid);
    long countThisMonthBoardAll(String delYn, String noticeYn);
}
