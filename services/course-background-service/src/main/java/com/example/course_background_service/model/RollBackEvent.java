package com.example.course_background_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RollBackEvent {
    private UUID eventId;
    private String correlationId ;
    private Long studentId;
    private String message;
    private long timestamp;
    private String token ;
    private String eventType;
}
