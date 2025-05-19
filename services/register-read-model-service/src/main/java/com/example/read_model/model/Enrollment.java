package com.example.read_model.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    private Long id;

    private Long studentId;

    private Long courseId;

    private String status;

    private Integer orderNumber;

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private boolean isActive;


}