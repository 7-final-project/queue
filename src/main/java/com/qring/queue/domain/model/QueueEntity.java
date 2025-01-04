package com.qring.queue.domain.model;

import com.qring.queue.domain.model.constraint.QueueStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_queue")
public class QueueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "queue_seq")
    @SequenceGenerator(name = "queue_seq", sequenceName = "queue_seq", allocationSize = 1)
    @Column(name = "queue_id", nullable = false)
    private Long id;

    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @Column(name = "number")
    private Long number;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QueueStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at" , nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Builder
    public QueueEntity(Long reservationId,
                       Long userId) {
        this.reservationId = reservationId;
        this.status = QueueStatus.WAITING;
        this.createdBy = "tempUser";
        this.modifiedBy = "tempUser";
    }

    @PostPersist
    public void setNumberAfterPersist() {
        this.number = this.id;
    }
}
