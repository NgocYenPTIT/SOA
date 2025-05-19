package com.example.read_model.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private Long id;

    private String code;

    private String courseGroup;

    private String practiseGroup;

    private Long subjectId;

    private Long semesterId;

    private Long instructorId;

    private Integer maxStudents;

    private Integer remainSlot;

    private Integer currentStudents ;

    private boolean isActive ;


    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

}