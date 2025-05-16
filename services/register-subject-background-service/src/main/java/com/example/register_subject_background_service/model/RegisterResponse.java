package com.example.register_subject_background_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegisterResponse {
    private Boolean success;
    private Long status;
    private String message;
}
