package com.example.userservice.config;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            // Kiểm tra xem đã có dữ liệu chưa
            if (userRepository.count() == 0) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();

                // Initialize 5 students
                // Student 1: specified user
                User student1 = new User();
                student1.setUsername("student1");
                student1.setPassword("1");
                student1.setFullName("Student ABC");
                student1.setEmail("abc@example.com");
                student1.setPhone("0123456789");
                student1.setRole("STUDENT");
                student1.setMajorId(1L); // Set default major ID, assuming it exists
                student1.setStudentId("B21DCCN123");
                student1.setSemesterId(1L);
                userRepository.save(student1);

                // Student 2
                User student2 = new User();
                student2.setUsername("student2");
                student2.setPassword("1");
                student2.setFullName("Student Two");
                student2.setEmail("student2s@example.com");
                student2.setPhone("0987654321");
                student2.setRole("STUDENT");
                student2.setMajorId(1L);
                student2.setStudentId("B21DCCN145");
                student2.setSemesterId(1L);
                userRepository.save(student2);


                // Teacher 1
                User teacher1 = new User();
                teacher1.setUsername("ghi");
                teacher1.setPassword("1");
                teacher1.setFullName("Teacher One");
                teacher1.setEmail("techasss2@example.com");
                teacher1.setPhone("0987654321");
                teacher1.setRole("TEACHER");
                teacher1.setMajorId(1L);
                teacher1.setStudentId("B21DCCN132");
                teacher1.setSemesterId(1L);
                userRepository.save(teacher1);

                // Teacher 1
                User teacher2 = new User();
                teacher2.setUsername("jkl");
                teacher2.setPassword("1");
                teacher2.setFullName("Teacher Two");
                teacher2.setEmail("techass2@example.com");
                teacher2.setPhone("0987654321");
                teacher2.setRole("TEACHER");
                teacher2.setMajorId(1L);
                teacher2.setStudentId("B21DCCN1456");
                teacher2.setSemesterId(1L);
                userRepository.save(teacher2);

                User student3 = new User();
                student3.setUsername("student3");
                student3.setPassword("1");
                student3.setFullName("Student Three");
                student3.setEmail("studeddnt3s@example.com");
                student3.setPhone("ddddd");
                student3.setRole("STUDENT");
                student3.setMajorId(1L);
                student3.setStudentId("B21DCCN125");
                student3.setSemesterId(1L);
                userRepository.save(student3);


            }
        };
    }
}
