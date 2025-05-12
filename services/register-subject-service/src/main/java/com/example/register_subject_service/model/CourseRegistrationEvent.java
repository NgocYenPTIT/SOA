package com.example.register_subject_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class CourseRegistrationEvent {
    private String eventId;
    private String studentId;
    private String courseId;
    private String action;
    private long timestamp;

    @JsonCreator
    public CourseRegistrationEvent(
            @JsonProperty("studentId") String studentId,
            @JsonProperty("courseId") String courseId,
            @JsonProperty("action") String action) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.studentId = studentId;
        this.courseId = courseId;
        this.action = action;
        this.timestamp = System.currentTimeMillis();
    }

}