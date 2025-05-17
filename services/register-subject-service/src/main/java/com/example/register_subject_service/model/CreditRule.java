package com.example.register_subject_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRule {
    private Long id;
    private Integer minCredits;
    private Integer maxCredits;

    private Long semesterId;

    private boolean isActive = true;

    private Date createdAt;
    private Date updatedAt;

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
        this.isActive = false;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}