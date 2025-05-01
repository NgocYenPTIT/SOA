package com.example.userservice.controller;

import com.example.userservice.DTOs.LoginRequest;
import com.example.userservice.DTOs.JwtResponse;

import com.example.userservice.model.User;
import com.example.userservice.security.JwtTokenProvider;
import com.example.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public UserController(JwtTokenProvider jwtTokenProvider, UserService userService ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserByUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(this.userService.getUserByUsername(username));
    }
}