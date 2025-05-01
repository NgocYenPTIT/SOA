package com.example.subject_service.controller;

import com.example.subject_service.security.JwtTokenProvider;
import com.example.subject_service.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubjectController {

    private final JwtTokenProvider jwtTokenProvider;
    private final SubjectService subjectService;

    public SubjectController(JwtTokenProvider jwtTokenProvider, SubjectService subjectService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.subjectService = subjectService;
    }
}