package com.example.register_subject_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class CourseRegistrationEvent {
    private UUID eventId;
    private String correlationId ;
    private Long studentId;
    private ArrayList<Long> courseIds;
    private long timestamp;
}