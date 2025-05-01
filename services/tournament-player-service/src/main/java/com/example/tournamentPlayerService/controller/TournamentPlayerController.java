package com.example.tournamentPlayerService.controller;

import com.example.tournamentPlayerService.DTOs.CreateTournamentRoundRequestDto;
import com.example.tournamentPlayerService.service.TournamentPlayerService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
@RestController
public class TournamentPlayerController {

    @Autowired
    private final TournamentPlayerService tournamentPlayerService;

    TournamentPlayerController(TournamentPlayerService tournamentPlayerService) {
        this.tournamentPlayerService = tournamentPlayerService;
    }

//    @PostMapping("/tournament-round")
//    public ResponseEntity<?> create(@RequestBody CreateTournamentRoundRequestDto form , HttpServletRequest request) {
//        return this.tournamentRoundService.create(form, request);
//    }

    @GetMapping("/tournament/{id}")
    public ResponseEntity<?> getListPlayers(@PathVariable Long id, @RequestParam("username") String username, @RequestParam("currentPage") Long currentPage, @RequestParam("pageSize") Long pageSize) {
        return this.tournamentPlayerService.getListPlayers(id,username,currentPage,pageSize);
    }
}