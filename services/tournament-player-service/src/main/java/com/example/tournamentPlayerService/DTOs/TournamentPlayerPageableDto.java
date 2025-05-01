package com.example.tournamentPlayerService.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TournamentPlayerPageableDto {
   
    private List<TournamentPlayerDto> players;
    private Integer pageSize ;
    private Integer totalItems;
    private Integer totalPages;
    private Integer currentPage;

}
