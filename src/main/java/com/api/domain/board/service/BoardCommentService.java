package com.api.domain.board.service;

import java.util.List;

import com.api.domain.board.dto.BoardDeleteRequest;
import com.api.domain.board.dto.CommentRequest;
import com.api.domain.board.dto.CommentResponse;

public interface BoardCommentService {

    List<CommentResponse> getComments(Long boardId);

    CommentResponse createComment(Long boardId, CommentRequest request, String uuid);

    void updateComment(Long boardId, Long commentId, CommentRequest request, String uuid);

    void deleteComment(Long boardId, Long commentId, BoardDeleteRequest request, String uuid);
}
