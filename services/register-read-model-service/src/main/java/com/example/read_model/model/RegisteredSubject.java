package com.example.read_model.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisteredSubject {
    private Long courseId;
    private String courseCode;
    private String courseGroup;
    private String practiseGroup;
    private String subjectName;
    private String subjectCode;
    private String enrollmentDate;
    private String Status;
    private Integer amountOfCredit;
    private List<ScheduleResponse> scheduleResponses;
}
