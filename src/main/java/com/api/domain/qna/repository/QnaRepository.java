package com.api.domain.qna.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.domain.qna.entity.QnaEntity;

@Repository
public interface QnaRepository extends JpaRepository<QnaEntity, Long>{
	
	Page<QnaEntity> findByDelYnAndAnswerYnAndTitleContaining(String delYn,String answerYn, String title, Pageable pageable);
    Page<QnaEntity> findByDelYnAndAnswerYn(String delYn, String answerYn, Pageable pageable);
    Optional<QnaEntity> findByQnaSeqAndDelYnAndAnswerYn(Long qnaSeq, String delYn, String answerYn);
}
