package com.api.domain.board.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.service.MemberService;
import com.api.domain.board.dto.BoardDeleteRequest;
import com.api.domain.board.dto.CommentRequest;
import com.api.domain.board.dto.CommentResponse;
import com.api.domain.board.entity.BoardCommentEntity;
import com.api.domain.board.entity.BoardEntity;
import com.api.domain.board.repository.BoardCommentRepository;
import com.api.domain.board.repository.BoardRepository;
import com.api.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCommentServiceImpl implements BoardCommentService {

    private final BoardCommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<CommentResponse> getComments(Long boardId) {
        return commentRepository
                .findByBoard_LocalIdAndDelYnOrderByCreatedDateAscLocalIdAsc(boardId, "N")
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentResponse createComment(Long boardId, CommentRequest request, String uuid) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException("게시글을 찾을 수 없습니다."));
        MemberEntity member = memberService.findByUuid(uuid);

        BoardCommentEntity comment = BoardCommentEntity.builder()
                .board(board)
                .userId(member.getUserId())
                .content(request.getContent())
                .build();

        return toResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void updateComment(Long boardId, Long commentId, CommentRequest request, String uuid) {
        BoardCommentEntity comment = findComment(commentId, boardId);
        MemberEntity member = memberService.findByUuid(uuid);

        if (!comment.getUserId().equals(member.getUserId())) {
            throw new BusinessException("수정 권한이 없습니다.");
        }
        comment.update(request.getContent());
    }

    @Override
    @Transactional
    public void deleteComment(Long boardId, Long commentId, BoardDeleteRequest request, String uuid) {
        BoardCommentEntity comment = findComment(commentId, boardId);
        MemberEntity member = memberService.findByUuid(uuid);

        if (!comment.getUserId().equals(member.getUserId())) {
            throw new BusinessException("삭제 권한이 없습니다.");
        }
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BusinessException("비밀번호가 틀립니다.");
        }
        comment.softDelete();
    }

    private BoardCommentEntity findComment(Long commentId, Long boardId) {
        BoardCommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException("댓글을 찾을 수 없습니다."));
        if (!comment.getBoard().getLocalId().equals(boardId)) {
            throw new BusinessException("댓글을 찾을 수 없습니다.");
        }
        if ("Y".equals(comment.getDelYn())) {
            throw new BusinessException("이미 삭제된 댓글입니다.");
        }
        return comment;
    }

    private CommentResponse toResponse(BoardCommentEntity c) {
        return CommentResponse.builder()
                .localId(c.getLocalId())
                .boardId(c.getBoard().getLocalId())
                .userId(c.getUserId())
                .content(c.getContent())
                .createdDate(c.getCreatedDate())
                .updatedDate(c.getUpdatedDate())
                .build();
    }
}
