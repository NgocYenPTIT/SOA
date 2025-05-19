package com.example.read_model.model;

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

public class UpdateReadModelEvent {
    private UUID eventId;
    private String correlationId ;
    private Long studentId;
    private boolean success;
    private String status;
    private  List<String> messages;
    private long timestamp;
    private String token ;
    private String eventType;
}