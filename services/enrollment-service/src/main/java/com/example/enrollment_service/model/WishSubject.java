package com.example.enrollment_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishSubject {

    private Long id;

    private Long studentId;

    private Long subjectId;

    private Long semesterId;

    private Date registerDate;

    private WishSubjectStatus status;

    private Date createdAt;

    private Date updatedAt;


    private Date deletedAt;

    private boolean isActive = true;

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
        this.isActive = false;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public enum WishSubjectStatus {
        PENDING, APPROVED, REJECTED
    }
}