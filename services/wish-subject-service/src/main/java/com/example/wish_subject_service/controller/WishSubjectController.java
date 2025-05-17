package com.example.wish_subject_service.controller;

import com.example.wish_subject_service.security.JwtTokenProvider;
import com.example.wish_subject_service.service.WishSubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WishSubjectController {

    private final JwtTokenProvider jwtTokenProvider;
    private final WishSubjectService wishSubjectService;

    public WishSubjectController(JwtTokenProvider jwtTokenProvider, WishSubjectService wishSubjectService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.wishSubjectService = wishSubjectService;
    }

    @GetMapping("/wish-subject")
    public ResponseEntity<?> getList(HttpServletRequest request) {
        return ResponseEntity.ok(wishSubjectService.getList((Long) request.getAttribute("id")));
    }
}