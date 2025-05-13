package com.example.register_subject_service.controller;

import com.example.register_subject_service.model.RegisterResponse;
import com.example.register_subject_service.model.RegisterSubjectRequest;
import com.example.register_subject_service.service.RegisterSubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class RegisterSubjectController {
    private final RegisterSubjectService registerSubjectService;

    public RegisterSubjectController(RegisterSubjectService registerSubjectService) {
        this.registerSubjectService = registerSubjectService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(HttpServletRequest request, @RequestBody RegisterSubjectRequest form) {
        List<RegisterResponse> result = this.registerSubjectService.registerSubject(request, form);

        if(result.size() == 1) return ResponseEntity.ok(result);

        return ResponseEntity.internalServerError().body("Error");
    }

}