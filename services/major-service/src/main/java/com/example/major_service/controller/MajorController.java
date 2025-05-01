package com.example.major_service.controller;

import com.example.major_service.security.JwtTokenProvider;
import com.example.major_service.service.MajorService;
import org.springframework.web.bind.annotation.*;

@RestController
public class MajorController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MajorService majorService;

    public MajorController(JwtTokenProvider jwtTokenProvider, MajorService majorService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.majorService = majorService;
    }
}