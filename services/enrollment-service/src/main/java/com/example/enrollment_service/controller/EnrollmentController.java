package com.example.enrollment_service.controller;

import com.example.enrollment_service.model.Enrollment;
import com.example.enrollment_service.security.JwtTokenProvider;
import com.example.enrollment_service.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class EnrollmentController {

    private  EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/enrollment")
    public ResponseEntity<?> getList(HttpServletRequest request) {
        return ResponseEntity.ok(enrollmentService.getListRegistered(request));
    }

//    @GetMapping("/enrollment-open")
//    public ResponseEntity<?> getListOpen(HttpServletRequest request) {
//        return ResponseEntity.ok(enrollmentService.getListOpen(request));
//    }
}