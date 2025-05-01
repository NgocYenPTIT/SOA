package com.example.enrollment_service.service;

import com.example.enrollment_service.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    private final EnrollmentRepository userRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository userRepository) {
        this.userRepository = userRepository;
    }

}