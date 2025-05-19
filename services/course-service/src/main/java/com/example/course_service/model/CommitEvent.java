package com.example.course_service.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CommitEvent {
    private UUID eventId;
    private String correlationId ;
    private Long studentId;
    private String message;
    private long timestamp;
    private String token ;
    private String eventType;
}
