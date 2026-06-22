package com.api.domain.board.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board_comment")
@DynamicUpdate
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCommentEntity {

    @Id
    @Column(name = "local_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long localId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private BoardEntity board;

    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "del_yn", length = 1)
    private String delYn;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDate.now();
        this.delYn = "N";
    }

    public void update(String content) {
        this.content = content;
        this.updatedDate = LocalDate.now();
    }

    public void softDelete() {
        this.delYn = "Y";
    }
}
