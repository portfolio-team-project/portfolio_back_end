package com.api.domain.qna.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.api.domain.base.Member.entity.MemberEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="qna")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnaEntity {
	@Id
	@Column(name="qna_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long qnaSeq;
	
	@Column(name="nickname", length = 50)
	private String nickname;
	
	@Column(name="reg_dt")
	private LocalDateTime regDt;
	
	@Column(name="modi_dt")
	private LocalDateTime modiDt;
	
	@Column(name="title", length = 100)
	private String title;
	
	@Column(name="content", length = 2000)
	private String content;
	
	@Column(name="ip_addr", length = 30)
	private String ipAddr;
	
	@Column(name="del_yn", length = 1)
	private String delYn;
	
	@Column(name="answer", length = 2000)
	private String answer;
	
	@Column(name="answer_yn", length = 1)
	private String answerYn;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = true)
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private MemberEntity member;
	
	@Column(name="answer_dt")
	private LocalDateTime answerDt;
	
	@Column(name="view_cnt")
	private Long viewCnt;
	
	@PrePersist
    public void prePersist() {
        this.regDt = LocalDateTime.now();
        this.delYn = "N";
        this.answerYn = "N";
        this.viewCnt = 0L;
    }
	
	@PreUpdate
	public void preUpdate() {
	    this.modiDt = LocalDateTime.now();
	}
	
	public void registerAnswer(String answer) {
	    this.answer = answer;
	    this.answerYn = "Y";
	    this.answerDt = LocalDateTime.now();
	}

	public void increaseViewCnt() {
	    this.viewCnt++;
	}

	public void softDelete() {
	    this.delYn = "Y";
	}
}
