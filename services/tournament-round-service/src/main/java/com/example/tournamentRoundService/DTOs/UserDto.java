package com.example.tournamentRoundService.DTOs;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Date dateOfBirth;
    private boolean gender;
    private String email;
    private String phoneNumber;
    private boolean isActive ;
    private Date lastAccess;
    private int elo;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
