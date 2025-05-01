package com.example.room_service.controller;

import com.example.room_service.security.JwtTokenProvider;
import com.example.room_service.service.RoomService;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RoomService roomService;

    public RoomController(JwtTokenProvider jwtTokenProvider, RoomService roomService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.roomService = roomService;
    }
}