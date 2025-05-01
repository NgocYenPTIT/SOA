package com.example.tournamentservice.repository;

import com.example.tournamentservice.model.Tournament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    boolean existsByName(String name);

    // 1. Lấy tất cả tournament với deletedAt là null
    List<Tournament> findByDeletedAtIsNull();
    Page<Tournament> findByDeletedAtIsNull(Pageable pageable);

    // 1. Lấy tất cả tournament với name và deletedAt là null
    List<Tournament> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String name);
    Page<Tournament> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String name, Pageable pageable);

    // 2. Lấy các tournament do người chơi tổ chức và tham gia (organizerId = playerId, join = true)
    Page<Tournament> findByOrganizerIdAndAttendTrueAndDeletedAtIsNull(Long organizerId, Pageable pageable);
    Page<Tournament> findByOrganizerIdAndAttendTrueAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long organizerId, String name, Pageable pageable);

    // 3. Lấy các tournament do người chơi chỉ tổ chức (organizerId = playerId, join = false)
    Page<Tournament> findByOrganizerIdAndAttendFalseAndDeletedAtIsNull(Long organizerId, Pageable pageable);
    Page<Tournament> findByOrganizerIdAndAttendFalseAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long organizerId, String name, Pageable pageable);

    // 4. Lấy các tournament người chơi chỉ tham gia (organizerId != playerId)
    Page<Tournament> findByOrganizerIdNotAndDeletedAtIsNull(Long organizerId, Pageable pageable);
    Page<Tournament> findByOrganizerIdNotAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long organizerId, String name, Pageable pageable);
}