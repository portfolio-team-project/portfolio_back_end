package com.api.domain.board.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board_list")
@DynamicUpdate
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardEntity {

    @Id
    @Column(name = "local_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long localId;

    @Column(name = "user_id", length = 10, nullable = false)
    private String userId;

    @Column(name = "notice_yn", length = 1, columnDefinition = "varchar(1) default 'N'")
    private String noticeYn;

    @Column(name = "title", length = 20)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "view_cnt")
    private Integer viewCnt;

    @Column(name = "like_cnt")
    private Integer likeCnt;

    @Column(name = "comment", length = 100)
    private String comment;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @Column(name = "del_yn", length = 1)
    private String delYn;

    @Column(name = "del_date")
    private LocalDate delDate;

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDate.now();
        this.delYn = "N";
        if (this.noticeYn == null) this.noticeYn = "N";
        this.viewCnt = 0;
        this.likeCnt = 0;
    }

    public void softDelete() {
        this.delYn = "Y";
        this.delDate = LocalDate.now();
    }

    public void increaseViewCnt() {
        this.viewCnt++;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedDate = LocalDate.now();
    }
}
