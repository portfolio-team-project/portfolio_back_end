package com.api.domain.base.Member.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="Find_Password")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindPasswordEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="password_seq")
    private Long passwordSeq; 
    
    @ManyToOne
    @JoinColumn(name="user_id")
    private MemberEntity member;
    
    @Column(name="email", length=30)
    private String email;
    
    @Column(name="certificate_num", length=50)
    private String certificateNum;
    
    @Column(name="reg_dt")
    private LocalDateTime regDt;
    
    @Column(name="use_yn", length=1)
    private String useYn;
}
