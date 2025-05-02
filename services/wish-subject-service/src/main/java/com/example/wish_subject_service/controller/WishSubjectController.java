package com.example.wish_subject_service.controller;

import com.example.wish_subject_service.security.JwtTokenProvider;
import com.example.wish_subject_service.service.WishSubjectService;
import org.springframework.web.bind.annotation.*;

@RestController
public class WishSubjectController {

    private final JwtTokenProvider jwtTokenProvider;
    private final WishSubjectService wishSubjectService;

    public WishSubjectController(JwtTokenProvider jwtTokenProvider, WishSubjectService wishSubjectService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.wishSubjectService = wishSubjectService;
    }
}