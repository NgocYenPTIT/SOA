package com.example.room_service.service;

import com.example.room_service.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository userRepository;

    @Autowired
    public RoomService(RoomRepository userRepository) {
        this.userRepository = userRepository;
    }

}