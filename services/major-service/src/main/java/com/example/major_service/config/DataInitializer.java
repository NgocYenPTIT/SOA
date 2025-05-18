
package com.example.major_service.config;

import com.example.major_service.model.Major;
import com.example.major_service.repository.MajorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(MajorRepository majorRepository) {
        return args -> {
            // Khởi tạo ngành học (Majors)
            if (majorRepository.count() == 0) {
                System.out.println("Bắt đầu khởi tạo dữ liệu ngành học...");

                // Major 1
                Major major1 = new Major();
                major1.setName("Công nghệ thông tin");
                majorRepository.save(major1);

                // Major 2
                Major major2 = new Major();
                major2.setName("Khoa học máy tính");
                majorRepository.save(major2);


                System.out.println("Đã khởi tạo " + majorRepository.count() + " ngành học vào database");
            }
        };
    }
}