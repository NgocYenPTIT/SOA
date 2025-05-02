package com.example.schedule_service.controller;

import com.example.schedule_service.DTOs.GetScheduleDto;
import com.example.schedule_service.security.JwtTokenProvider;
import com.example.schedule_service.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ScheduleController {

    private final JwtTokenProvider jwtTokenProvider;
    private final ScheduleService scheduleService;

    public ScheduleController(JwtTokenProvider jwtTokenProvider, ScheduleService scheduleService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.scheduleService = scheduleService;
    }

    @GetMapping("/schedule")
    public ResponseEntity<?> getList(HttpServletRequest request, GetScheduleDto form) {
        return ResponseEntity.ok(scheduleService.getList(request, form));
    }

}