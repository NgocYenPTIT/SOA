package com.example.authservice.controller;

import com.example.authservice.DTOs.LoginRequest;

import com.example.authservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController( AuthService authService ) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return  this.authService.login(loginRequest);
    }

    @PostMapping("/validate-active-user/{id}")
    public ResponseEntity<?> validateActiveUser(@PathVariable("id") Long id, HttpServletRequest request) {
        System.out.println("xin chao ");
        return  this.authService.validateActiveUser(id, (String)request.getAttribute("token"));
    }

    @GetMapping("/test-token")
    public ResponseEntity<?> testToken(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        Long id = (Long) request.getAttribute("id");
        System.out.println("ID: " + id + ", Username: " + username); // Debug
        return ResponseEntity.ok("ID: " + id + ", Username: " + username);
    }

}