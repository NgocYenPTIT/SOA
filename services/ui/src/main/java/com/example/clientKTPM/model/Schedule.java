package com.example.clientKTPM.model;

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

    private boolean isActive = true;

}