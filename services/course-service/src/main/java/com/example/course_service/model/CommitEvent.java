package com.example.course_service.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CommitEvent {
    private UUID eventId;
    private String correlationId ;
    private Long studentId;
    private List<List<Long>> addAndDeleteCourses;
    private long timestamp;
    private String token ;
    private String eventType;
}
