package com.example.read_model.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;

    private String username;

    private String password;

    private String fullName;

    private String email;

    private String phone;

    private String role;

    private String studentId;

    private Long semesterId;

    private Long majorId;

    private boolean isActive;

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

}
