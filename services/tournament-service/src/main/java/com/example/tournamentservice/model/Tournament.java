package com.example.tournamentservice.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "tournaments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private boolean freeToJoin;
    private boolean attend;
    private String description;
    private Long organizerId;

    // Giữ lại khóa ngoại như một thuộc tính bình thường
    @Column(name = "board_type_id")
    private Long boardTypeId;

    // Giữ lại khóa ngoại như một thuộc tính bình thường
    @Column(name = "organizing_method_id")
    private Long organizingMethodId;

    // Thêm quan hệ ManyToOne với BoardType
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private BoardType boardType;

    // Thêm quan hệ ManyToOne với OrganizingMethod
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizing_method_id", referencedColumnName = "id", insertable = false, updatable = false)
    private OrganizingMethod organizingMethod;

    private Integer maxPlayer;

    @Column(name = "start_date")
    private String startDate;  // Lưu dưới định dạng DD/MM/YYYY HH:MM

    @Column(name = "end_date")
    private String endDate;    // Lưu dưới định dạng DD/MM/YYYY HH:MM

    @Column(name = "status")
    private String status;     // Trạng thái của giải đấu

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
    }

    // Method để check xem user đã bị xóa chưa
    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}