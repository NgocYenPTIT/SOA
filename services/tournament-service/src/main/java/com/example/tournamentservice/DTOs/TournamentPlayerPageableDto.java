package com.example.tournamentservice.DTOs;

import com.example.tournamentservice.DTOs.TournamentPlayerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
