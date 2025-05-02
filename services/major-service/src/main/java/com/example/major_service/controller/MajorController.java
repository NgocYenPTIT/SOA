package com.example.major_service.controller;

import com.example.major_service.model.Major;
import com.example.major_service.repository.MajorRepository;
import com.example.major_service.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MajorController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MajorRepository majorRepository;

    public MajorController(JwtTokenProvider jwtTokenProvider, MajorRepository majorRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.majorRepository = majorRepository;
    }

    @GetMapping("/majors")
    public ResponseEntity<List<Major>> getAllMajors() {
        List<Major> majors = majorRepository.findAll();
        return ResponseEntity.ok(majors);
    }

    @GetMapping("/majors/{id}")
    public ResponseEntity<Major> getMajorById(@PathVariable Long id) {
        Optional<Major> major = majorRepository.findById(id);
        if (major.isPresent()) {
            return ResponseEntity.ok(major.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}