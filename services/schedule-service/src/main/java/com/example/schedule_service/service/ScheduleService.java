package com.example.schedule_service.service;

import com.example.schedule_service.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private final ScheduleRepository userRepository;

    @Autowired
    public ScheduleService(ScheduleRepository userRepository) {
        this.userRepository = userRepository;
    }

}