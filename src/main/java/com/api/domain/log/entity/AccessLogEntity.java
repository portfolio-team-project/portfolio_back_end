package com.api.domain.log.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="ACCESS_LOG")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessLogEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "accessSeq")
    private Long access_seq;
	
	@Column(name = "access_ip", length = 50)
    private String accessIp;
	
	@Column(name = "access_uri", length = 200)
    private String accessUri;
	
	@Column(name = "method", length = 50)
    private String method;
	
	@Column(name = "access_dt")
    private LocalDateTime accessDt;
}
