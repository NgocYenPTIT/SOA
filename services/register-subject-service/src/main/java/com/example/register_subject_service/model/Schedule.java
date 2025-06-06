package com.example.register_subject_service.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    private Long id;
    private Long courseId;
    private Long roomId;
    private Long teacherId;
    private Date startTime;
    private Date endTime;
    private String type;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Boolean deleted;
    private boolean isActive;
}