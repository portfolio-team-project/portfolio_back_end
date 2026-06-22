package com.api.domain.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.service.MemberService;
import com.api.global.exception.BusinessException;
import com.api.domain.board.dto.BoardDeleteRequest;
import com.api.domain.board.dto.BoardDetailResponse;
import com.api.domain.board.dto.BoardListResponse;
import com.api.domain.board.dto.BoardPageResponse;
import com.api.domain.board.dto.BoardRequest;
import com.api.domain.board.entity.BoardEntity;
import com.api.domain.board.repository.BoardRepository;
import com.api.global.util.HtmlSanitizer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final MemberService memberService;
    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createBoard(BoardRequest boardRequest, String uuid, boolean isAdmin, HttpServletRequest request) {
        MemberEntity member = memberService.findByUuid(uuid);

        boardRepository.save(BoardEntity.builder()
                .title(HtmlSanitizer.sanitize(boardRequest.getTitle()))
                .content(HtmlSanitizer.sanitize(boardRequest.getContent()))
                .userId(member.getUserId())
                .noticeYn(isAdmin ? "Y" : "N")
                .build());
    }

    @Override
    public BoardPageResponse getBoardList(Pageable pageable, String title) {
        List<BoardEntity> notices = boardRepository
                .findTop2NoticesByLatestDate("Y", "N", PageRequest.of(0, 2));

        int regularSize = Math.max(1, pageable.getPageSize() - notices.size());
        Pageable sortedPage = PageRequest.of(pageable.getPageNumber(), regularSize,
                Sort.by(Sort.Direction.DESC, "createdDate").and(Sort.by(Sort.Direction.DESC, "localId")));

        Page<BoardEntity> regularPage;
        if (title == null || title.isBlank()) {
            regularPage = boardRepository.findByNoticeYnAndDelYn("N", "N", sortedPage);
        } else {
            regularPage = boardRepository.findByNoticeYnAndDelYnAndTitleContaining("N", "N", title, sortedPage);
        }

        List<BoardListResponse> combined = new ArrayList<>();
        notices.stream().map(this::toListResponse).forEach(combined::add);
        regularPage.getContent().stream().map(this::toListResponse).forEach(combined::add);

        return BoardPageResponse.builder()
                .content(combined)
                .page(new BoardPageResponse.PageInfo(
                        regularPage.getTotalElements(),
                        Math.max(1, regularPage.getTotalPages()),
                        pageable.getPageNumber()))
                .build();
    }

    @Override
    @Transactional
    public void updateBoard(Long localId, BoardRequest boardRequest, String uuid) {
        BoardEntity board = boardRepository.findById(localId)
                .orElseThrow(() -> new BusinessException("게시글을 찾을 수 없습니다."));
        MemberEntity member = memberService.findByUuid(uuid);
        if (!board.getUserId().equals(member.getUserId())) {
            throw new BusinessException("수정 권한이 없습니다.");
        }
        board.update(
                HtmlSanitizer.sanitize(boardRequest.getTitle()),
                HtmlSanitizer.sanitize(boardRequest.getContent())
        );
    }

    @Override
    @Transactional
    public BoardDetailResponse getBoardDetail(Long localId) {
        BoardEntity board = boardRepository.findById(localId)
                .orElseThrow(() -> new BusinessException("게시글을 찾을 수 없습니다."));
        board.increaseViewCnt();
        return BoardDetailResponse.builder()
                .localId(board.getLocalId())
                .userId(board.getUserId())
                .title(board.getTitle())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .viewCnt(board.getViewCnt())
                .likeCnt(board.getLikeCnt())
                .build();
    }

    @Override
    @Transactional
    public void deleteBoard(Long localId, BoardDeleteRequest boardDeleteRequest, String uuid) {
        BoardEntity board = boardRepository.findById(localId)
                .orElseThrow(() -> new BusinessException("게시글을 찾을 수 없습니다."));
        MemberEntity member = memberService.findByUuid(uuid);
        if (!board.getUserId().equals(member.getUserId())) {
            throw new BusinessException("삭제 권한이 없습니다.");
        }
        if (!passwordEncoder.matches(boardDeleteRequest.getPassword(), member.getPassword())) {
            throw new BusinessException("비밀번호가 틀립니다.");
        }
        board.softDelete();
    }

    private BoardListResponse toListResponse(BoardEntity b) {
        return BoardListResponse.builder()
                .localId(b.getLocalId())
                .userId(b.getUserId())
                .title(b.getTitle())
                .createdDate(b.getCreatedDate())
                .viewCnt(b.getViewCnt())
                .likeCnt(b.getLikeCnt())
                .noticeYn(b.getNoticeYn())
                .build();
    }
}
