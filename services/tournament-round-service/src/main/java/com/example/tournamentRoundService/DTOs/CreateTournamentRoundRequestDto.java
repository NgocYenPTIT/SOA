package com.example.tournamentRoundService.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTournamentRoundRequestDto {
    private Long tournamentId;
    private Integer roundNumber;
    private String status;
    private String startDate;
    private String endDate;

}