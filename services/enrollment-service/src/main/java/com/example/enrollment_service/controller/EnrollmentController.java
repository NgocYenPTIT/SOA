package com.example.enrollment_service.controller;

import com.example.enrollment_service.security.JwtTokenProvider;
import com.example.enrollment_service.service.EnrollmentService;
import org.springframework.web.bind.annotation.*;

@RestController
public class EnrollmentController {

    private final JwtTokenProvider jwtTokenProvider;
    private final EnrollmentService enrollmentService;

    public EnrollmentController(JwtTokenProvider jwtTokenProvider, EnrollmentService enrollmentService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.enrollmentService = enrollmentService;
    }
}