package com.example.room_service.config;

import com.example.room_service.model.Room;
import com.example.room_service.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(RoomRepository roomRepository) {
        return args -> {
            // Khởi tạo phòng học (Rooms)
            if (roomRepository.count() == 0) {
                System.out.println("Bắt đầu khởi tạo dữ liệu phòng học...");

                // Room 1
                Room room1 = new Room();
                room1.setRoomCode("A101");
                roomRepository.save(room1);

                // Room 2
                Room room2 = new Room();
                room2.setRoomCode("A102");
                roomRepository.save(room2);

                // Room 3
                Room room3 = new Room();
                room3.setRoomCode("A103");
                roomRepository.save(room3);

                // Room 4
                Room room4 = new Room();
                room4.setRoomCode("B201");
                roomRepository.save(room4);

                // Room 5
                Room room5 = new Room();
                room5.setRoomCode("B202");
                roomRepository.save(room5);

                // Room 6
                Room room6 = new Room();
                room6.setRoomCode("B203");
                roomRepository.save(room6);

                // Room 7
                Room room7 = new Room();
                room7.setRoomCode("C301");
                roomRepository.save(room7);

                // Room 8
                Room room8 = new Room();
                room8.setRoomCode("C302");
                roomRepository.save(room8);

                // Room 9
                Room room9 = new Room();
                room9.setRoomCode("LAB01");
                roomRepository.save(room9);

                // Room 10
                Room room10 = new Room();
                room10.setRoomCode("LAB02");
                roomRepository.save(room10);

                System.out.println("Đã khởi tạo " + roomRepository.count() + " phòng học vào database");
            }
        };
    }
}