package com.example.course_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction_log",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"correlation_id", "status"},
                        name = "duplicate")
        })
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "correlation_id")
    private String correlationId;

    @Column(nullable = false)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;


    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
        if (updatedAt == null) {
            updatedAt = new Date();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public void delete() {
        this.deletedAt = new Date();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
