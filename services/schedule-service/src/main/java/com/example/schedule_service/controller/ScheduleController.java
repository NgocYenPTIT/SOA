package com.example.schedule_service.controller;

import com.example.schedule_service.security.JwtTokenProvider;
import com.example.schedule_service.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScheduleController {

    private final JwtTokenProvider jwtTokenProvider;
    private final ScheduleService scheduleService;

    public ScheduleController(JwtTokenProvider jwtTokenProvider, ScheduleService scheduleService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.scheduleService = scheduleService;
    }
}