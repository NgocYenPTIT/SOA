package com.example.semester_service.service;

import com.example.semester_service.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SemesterService {

    private final SemesterRepository userRepository;

    @Autowired
    public SemesterService(SemesterRepository userRepository) {
        this.userRepository = userRepository;
    }

}