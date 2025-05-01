package com.example.clientKTPM.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentOverviewDto {
    private Long id;
    private String name;
    private String participantNum;
    private String startDate;
    private String endDate;
    private String organizer;
    private String status;
}