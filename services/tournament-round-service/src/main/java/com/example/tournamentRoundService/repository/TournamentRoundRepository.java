package com.example.tournamentRoundService.repository;

import com.example.tournamentRoundService.model.TournamentRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRoundRepository extends JpaRepository<TournamentRound, Long> {
    List<TournamentRound> findByTournamentId(Long id);
}