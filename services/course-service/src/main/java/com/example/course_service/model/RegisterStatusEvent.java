package com.example.course_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class RegisterStatusEvent {
    private UUID eventId;
    private String correlationId ;
    private Long studentId;
    private boolean success;
    private List<String> messages;
    private long timestamp;
    private String token ;
    private String eventType;

}