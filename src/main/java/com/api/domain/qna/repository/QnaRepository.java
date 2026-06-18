package com.api.domain.qna.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.domain.qna.entity.QnaEntity;

@Repository
public interface QnaRepository extends JpaRepository<QnaEntity, Long>{
	
	Page<QnaEntity> findByDelYnAndTitleContaining(String delYn, String title, Pageable pageable);
    Page<QnaEntity> findByDelYn(String delYn, Pageable pageable);
    Optional<QnaEntity> findByQnaSeqAndDelYn(Long qnaSeq, String delYn);
}
