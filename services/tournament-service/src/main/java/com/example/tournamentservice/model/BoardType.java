package com.example.tournamentservice.model;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "board_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String size;

    private String description;

    private Boolean isActive;

    // Thêm các trường audit
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    // Method này sẽ tự động được gọi trước khi entity được lưu vào DB lần đầu
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
        if (updatedAt == null) {
            updatedAt = new Date();
        }
    }

    // Method này sẽ tự động được gọi trước khi entity được cập nhật
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    // Method để soft delete user
    public void delete() {
        this.deletedAt = new Date();
        this.isActive = false;
    }

    // Method để check xem user đã bị xóa chưa
    public boolean isDeleted() {
        return this.deletedAt != null;
    }


}