package com.example.read_model.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private Long id;

    private String roomCode;

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private boolean isActive = true;

}