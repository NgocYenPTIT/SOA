package com.example.userservice.config;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            // Kiểm tra xem đã có dữ liệu chưa
            if (userRepository.count() == 0) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();

                // User 1 - Admin
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("password");
                admin.setDateOfBirth(dateFormat.parse("1990-01-15"));
                admin.setGender(true);
                admin.setEmail("admin@example.com");
                admin.setPhoneNumber("0987654321");
                admin.setLastAccess(now);
//                admin.setElo(2000);


                // User 2 - Người chơi cờ tích cực
                User activePlayer = new User();
                activePlayer.setUsername("player1");
                activePlayer.setPassword("chess123");
                activePlayer.setDateOfBirth(dateFormat.parse("1995-06-21"));
                activePlayer.setGender(true);
                activePlayer.setEmail("player1@example.com");
                activePlayer.setPhoneNumber("0901234567");
                activePlayer.setLastAccess(now);
//                activePlayer.setElo(1750);


                // User 3 - Người chơi nữ
                User femalePlayer = new User();
                femalePlayer.setUsername("player2");
                femalePlayer.setPassword("chess456");
                femalePlayer.setDateOfBirth(dateFormat.parse("1998-03-08"));
                femalePlayer.setGender(false);
                femalePlayer.setEmail("player2@example.com");
                femalePlayer.setPhoneNumber("0912345678");
                femalePlayer.setLastAccess(now);
//                femalePlayer.setElo(1620);


                // Lưu cả 3 bản ghi vào database
                userRepository.save(admin);
                userRepository.save(activePlayer);
                userRepository.save(femalePlayer);

                System.out.println("Đã khởi tạo 3 bản ghi User vào database");
            }
        };
    }
}
