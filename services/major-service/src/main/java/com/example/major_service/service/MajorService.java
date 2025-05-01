package com.example.major_service.service;

import com.example.major_service.repository.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MajorService {

    private final MajorRepository userRepository;

    @Autowired
    public MajorService(MajorRepository userRepository) {
        this.userRepository = userRepository;
    }

}