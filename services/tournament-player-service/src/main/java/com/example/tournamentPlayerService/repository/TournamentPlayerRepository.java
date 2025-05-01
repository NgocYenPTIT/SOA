package com.example.tournamentPlayerService.repository;

import com.example.tournamentPlayerService.model.TournamentPlayer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentPlayerRepository extends JpaRepository<TournamentPlayer, Long> {
    // Tìm theo tournamentId và deletedAt là null
    List<TournamentPlayer> findByTournamentIdAndDeletedAtIsNull(Long tournamentId);

    // Tìm theo tournamentId, playerName và deletedAt là null với phân trang
    Page<TournamentPlayer> findByTournamentIdAndPlayerNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long tournamentId, String playerName, Pageable pageable);

    // Tìm theo tournamentId và deletedAt là null với phân trang
    Page<TournamentPlayer> findByTournamentIdAndDeletedAtIsNull(Long tournamentId, Pageable pageable);
}