package com.api.domain.board.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.board.dto.BoardDeleteRequest;
import com.api.domain.board.dto.BoardDetailResponse;
import com.api.domain.board.dto.BoardPageResponse;
import com.api.domain.board.dto.BoardRequest;
import com.api.domain.board.service.BoardService;
import com.api.global.common.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/write")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> writeBoard(@Valid @RequestBody BoardRequest boardRequest,
                                                        @AuthenticationPrincipal String uuid,
                                                        HttpServletRequest request,
                                                        Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boardService.createBoard(boardRequest, uuid, isAdmin, request);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/{localId}")
    public ResponseEntity<ApiResponse<BoardDetailResponse>> getBoardDetail(@PathVariable Long localId) {
        return ResponseEntity.ok(ApiResponse.ok(boardService.getBoardDetail(localId)));
    }

    @PutMapping("/{localId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateBoard(@PathVariable Long localId,
                                                         @Valid @RequestBody BoardRequest boardRequest,
                                                         @AuthenticationPrincipal String uuid) {
        boardService.updateBoard(localId, boardRequest, uuid);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @DeleteMapping("/{localId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(@PathVariable Long localId,
                                                         @Valid @RequestBody BoardDeleteRequest boardDeleteRequest,
                                                         @AuthenticationPrincipal String uuid) {
        boardService.deleteBoard(localId, boardDeleteRequest, uuid);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<BoardPageResponse>> getBoardList(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 10);
        BoardPageResponse boardList = boardService.getBoardList(pageable, title);
        return ResponseEntity.ok(ApiResponse.ok(boardList));
    }
}
