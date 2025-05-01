package com.example.clientKTPM.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetListTournamentDto {
    private Long id;
    private String name;
    private String status;
    private String startDate;
    private String endDate;
    private String organizer;
    private String participantNum;
}
