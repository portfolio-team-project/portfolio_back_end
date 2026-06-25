package com.api.domain.base.Login.entity;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="login_log")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginEntity {
    
    @Id
    @Column(name="log_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logSeq;
    
    @ManyToOne
    @JoinColumn(name="user_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private MemberEntity member;
    
    @Column(name="user_ip", length=50)
    private String userIp;
    
    @Column(name="access_dt")
    private LocalDateTime accessDt;
    
    @Column(name="request_uri", length=200)
    private String requestUri;
    
    @Column(name="user_agent", length=1000)
    private String userAgent;
    
    @Column(name="login_yn", length=1)
    private String loginYn;
    
    @Column(name="fail_reason", length=3000)
    private String failReason;
    
    @PrePersist
    public void prePersist() {
        this.accessDt = LocalDateTime.now();
    }
}
