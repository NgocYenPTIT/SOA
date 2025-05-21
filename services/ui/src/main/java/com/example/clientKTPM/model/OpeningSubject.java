package com.example.clientKTPM.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningSubject {
    private Long courseId;
    private String subjectName;
    private String subjectCode;
    private String courseCode;
    private String courseGroup;
    private String practiseGroup;
    private Integer amountOfCredit;
    private Integer remainSlot;
    private Integer maxStudent;
    private List<ScheduleResponse> scheduleResponses;
}
