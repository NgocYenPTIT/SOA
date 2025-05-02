package com.example.room_service.controller;

import com.example.room_service.model.Room;
import com.example.room_service.security.JwtTokenProvider;
import com.example.room_service.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RoomService roomService;

    public RoomController(JwtTokenProvider jwtTokenProvider, RoomService roomService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.roomService = roomService;
    }
    
    /**
     * Get a room by its ID
     * @param id The room ID
     * @return Room object
     */
    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }
}