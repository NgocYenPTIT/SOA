package com.example.semester_service.controller;

import com.example.semester_service.security.JwtTokenProvider;
import com.example.semester_service.service.SemesterService;
import org.springframework.http.ResponseEntity;
import com.example.semester_service.model.Semester;
import org.springframework.web.bind.annotation.*;

@RestController
public class SemesterController {

    private final JwtTokenProvider jwtTokenProvider;
    private final SemesterService semesterService;

    public SemesterController(JwtTokenProvider jwtTokenProvider, SemesterService semesterService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.semesterService = semesterService;
    }

    @GetMapping("/semester/{id}")
    public ResponseEntity<Semester> getSemesterById(@PathVariable Long id) {
        Semester semester = semesterService.findSemesterById(id);
        if (semester != null) {
            return ResponseEntity.ok(semester);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}