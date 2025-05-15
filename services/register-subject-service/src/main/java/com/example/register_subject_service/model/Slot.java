package com.example.register_subject_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Slot {
    private Long id ;
    private String name;
    private Integer quantity;
}
