package com.example.enrollment_background_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class ChangeQuantitySlotEvent {
    private UUID eventId;
    private String correlationId ;
    private Long studentId;
    private List<List<Long>> addAndDeleteCourses;
    private long timestamp;
    private String token ;
    private String eventType;

}