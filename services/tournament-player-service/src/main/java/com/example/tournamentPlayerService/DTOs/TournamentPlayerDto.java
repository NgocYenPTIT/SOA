package com.example.tournamentPlayerService.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TournamentPlayerDto {
    private Long id;
    private Long tournamentId;
    private Long playerId;
    private String playerName;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;


}
