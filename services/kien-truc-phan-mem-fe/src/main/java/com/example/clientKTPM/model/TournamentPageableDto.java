package com.example.clientKTPM.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TournamentPageableDto {
    private List<TournamentOverviewDto> tournaments;
    private Integer pageSize ;
    private Integer totalItems;
    private Integer totalPages;
    private Integer currentPage;
}
