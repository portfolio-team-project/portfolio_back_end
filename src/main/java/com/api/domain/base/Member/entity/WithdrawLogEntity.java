package com.api.domain.base.Member.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="withdraw_log", indexes = @Index(name = "idx_withdraw_log_user_id_delete_dt", columnList = "user_id, delete_dt"))
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawLogEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_seq")
    private Long logSeq;
	
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@Column(name = "delete_dt")
    private LocalDateTime deleteDt;
}
