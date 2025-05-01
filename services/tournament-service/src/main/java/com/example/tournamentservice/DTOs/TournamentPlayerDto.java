package com.example.tournamentservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentPlayerDto {
    private Long id;

    private Long tournamentId;

    private Long playerId;

    private String status;     // Trạng thái của giải đấu

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;
}
