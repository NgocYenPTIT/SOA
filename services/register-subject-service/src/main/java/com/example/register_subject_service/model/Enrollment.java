package com.example.register_subject_service.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    private Long id;

    private Long studentId;

    private Long courseId;
    private Date enrollmentDate;

    private String status;

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

}