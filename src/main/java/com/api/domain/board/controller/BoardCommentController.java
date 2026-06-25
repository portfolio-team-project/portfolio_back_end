package com.api.domain.board.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.board.dto.BoardDeleteRequest;
import com.api.domain.board.dto.CommentRequest;
import com.api.domain.board.dto.CommentResponse;
import com.api.domain.board.service.BoardCommentService;
import com.api.global.common.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/board/{boardId}/comments")
@RequiredArgsConstructor
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(@PathVariable Long boardId) {
        return ResponseEntity.ok(ApiResponse.ok(boardCommentService.getComments(boardId)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long boardId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal String uuid) {
        return ResponseEntity.ok(ApiResponse.ok(boardCommentService.createComment(boardId, request, uuid)));
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal String uuid) {
        boardCommentService.updateComment(boardId, commentId, request, uuid);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @Valid @RequestBody BoardDeleteRequest request,
            @AuthenticationPrincipal String uuid) {
        boardCommentService.deleteComment(boardId, commentId, request, uuid);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
