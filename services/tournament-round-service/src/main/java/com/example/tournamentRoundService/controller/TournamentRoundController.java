package com.example.tournamentRoundService.controller;

import com.example.tournamentRoundService.DTOs.CreateTournamentRoundRequestDto;
import com.example.tournamentRoundService.service.TournamentRoundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TournamentRoundController {

    private final TournamentRoundService tournamentRoundService;

    public TournamentRoundController(TournamentRoundService tournamentRoundService ) {
        this.tournamentRoundService = tournamentRoundService;
    }


    @PostMapping("/tournament-round")
    public ResponseEntity<?> create(@RequestBody CreateTournamentRoundRequestDto form , HttpServletRequest request) {
        return this.tournamentRoundService.create(form, request);
    }

    @GetMapping("/tournament/{id}")
    public ResponseEntity<?> getListRound(@PathVariable Long id) {
        return this.tournamentRoundService.getListRound(id);
    }


}