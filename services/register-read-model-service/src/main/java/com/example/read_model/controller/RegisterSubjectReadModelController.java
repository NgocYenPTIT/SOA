package com.example.read_model.controller;

import com.example.read_model.service.RegisterSubjectReadModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RegisterSubjectReadModelController {
    private final RegisterSubjectReadModelService registerSubjectReadModelService;

    public RegisterSubjectReadModelController(RegisterSubjectReadModelService registerSubjectReadModelService) {
        this.registerSubjectReadModelService = registerSubjectReadModelService;
    }

    @GetMapping("/register-subject-read-model")
    public ResponseEntity<?> getView(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("id");
        String token = (String) request.getAttribute("token");
        System.out.println(token);
        return  ResponseEntity.ok(this.registerSubjectReadModelService.getView(id,token));
    }


}