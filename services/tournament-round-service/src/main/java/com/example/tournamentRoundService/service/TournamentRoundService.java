package com.example.tournamentRoundService.service;

import com.example.tournamentRoundService.DTOs.CreateTournamentRoundRequestDto;
import com.example.tournamentRoundService.model.TournamentRound;
import com.example.tournamentRoundService.repository.TournamentRoundRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Service
public class TournamentRoundService {
    @Autowired
    private TournamentRoundRepository tournamentRoundRepository;

    @Transactional
    public ResponseEntity<?> create(CreateTournamentRoundRequestDto form, HttpServletRequest request) {
        // Validate input
        if (form.getTournamentId() == null || form.getRoundNumber() == null ||
                form.getStatus() == null || form.getStartDate() == null || form.getEndDate() == null) {
            return ResponseEntity.badRequest().body("All fields are required");
        }

        try {
            // Convert DTO to entity
            TournamentRound tournamentRound = new TournamentRound();
            tournamentRound.setTournamentId(form.getTournamentId());
            tournamentRound.setRoundNumber(form.getRoundNumber());
            tournamentRound.setStatus(form.getStatus());
            tournamentRound.setStartDate(form.getStartDate());
            tournamentRound.setEndDate(form.getEndDate());

            // Save the entity
            TournamentRound savedRound = tournamentRoundRepository.save(tournamentRound);

            // Create success response
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Tournament round created successfully");
            response.put("data", savedRound);

            // Return successful response
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Create error response
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to create tournament round");
            errorResponse.put("error", e.getMessage());

            // Return error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    public ResponseEntity<?> getListRound(Long id) {
        List<TournamentRound> rounds = this.tournamentRoundRepository.findByTournamentId(id);
        return ResponseEntity.ok(rounds);
    }
    }