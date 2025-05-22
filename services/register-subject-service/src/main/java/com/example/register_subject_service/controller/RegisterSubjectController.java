package com.example.register_subject_service.controller;

import com.example.register_subject_service.model.RegisterResponse;
import com.example.register_subject_service.model.RegisterSubjectRequest;
import com.example.register_subject_service.model.ValidateFirstForm;
import com.example.register_subject_service.service.ProcessRegisterService;
import com.example.register_subject_service.service.RegisterSubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class RegisterSubjectController {
    private final RegisterSubjectService registerSubjectService;
    private final ProcessRegisterService processRegisterService;

    public RegisterSubjectController(RegisterSubjectService registerSubjectService, ProcessRegisterService processRegisterService) {
        this.registerSubjectService = registerSubjectService;
        this.processRegisterService = processRegisterService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(HttpServletRequest request, @RequestBody RegisterSubjectRequest form) {
        return  ResponseEntity.ok( this.registerSubjectService.registerSubject(request, form));
    }

    @GetMapping("/register")
    public ResponseEntity<?>validateFirst (HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        Long studentId = (Long) request.getAttribute("id");
        boolean canRegister = this.processRegisterService.validateFirst(studentId, token);
        if (!canRegister) {
            return  ResponseEntity.ok(ValidateFirstForm.builder().success("FAIL").build());

        }
        return  ResponseEntity.ok(ValidateFirstForm.builder().success("SUCCESS").build());
    }

}