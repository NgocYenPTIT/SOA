package com.example.clientKTPM.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentRoundDto {
    private Long id;
    private Long tournamentId;
    private Integer roundNumber;
    private String startDate;
    private String endDate;
    private String status;

}