package com.example.room_service.service;

import com.example.room_service.model.Room;
import com.example.room_service.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    
    /**
     * Find a room by its ID
     * @param id Room ID
     * @return Optional containing the room if found
     */
    public Optional<Room> findRoomById(Long id) {
        return roomRepository.findById(id);
    }
    
    /**
     * Get a room directly by its ID without Optional handling
     * @param id Room ID
     * @return Room object
     */
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).get();
    }
}