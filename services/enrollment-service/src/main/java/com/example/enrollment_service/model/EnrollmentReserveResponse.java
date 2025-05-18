package com.example.enrollment_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentReserveResponse {
    private String status ;
    private Enrollment enrollment;
}
