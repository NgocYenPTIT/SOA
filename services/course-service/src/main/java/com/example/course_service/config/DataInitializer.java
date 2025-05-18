package com.example.course_service.config;

import com.example.course_service.model.Course;
import com.example.course_service.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(CourseRepository courseRepository) {
        return args -> {
            // Khởi tạo các khóa học (Courses)
            if (courseRepository.count() == 0) {
                System.out.println("Bắt đầu khởi tạo dữ liệu khóa học...");

                // Course 1
                Course course1 = new Course();
                course1.setCode("COURSE001");
                course1.setCourseGroup("Nhóm 01");
                course1.setPractiseGroup("Tổ TH01");
                course1.setSubjectId(1L); // Giả sử có Subject với ID 1
                course1.setSemesterId(1L); // Giả sử có Semester với ID 1
                course1.setInstructorId(2L); // Teacher với ID 6 (teacher1)
                course1.setMaxStudents(2);
                course1.setRemainSlot(2);
                course1.setCurrentStudents(0);
                courseRepository.save(course1);

                // Course 2
                Course course2 = new Course();
                course2.setCode("COURSE002");
                course2.setCourseGroup("Nhóm 02");
                course2.setPractiseGroup("Tổ TH02");
                course2.setSubjectId(2L);
                course2.setSemesterId(1L);
                course2.setInstructorId(2L); // Teacher với ID 7 (teacher2)
                course2.setMaxStudents(2);
                course2.setRemainSlot(2);
                course2.setCurrentStudents(0);
                courseRepository.save(course2);

                // Course 3
                Course course3 = new Course();
                course3.setCode("COURSE003");
                course3.setCourseGroup("Nhóm 01");
                course3.setPractiseGroup("Tổ TH01");
                course3.setSubjectId(3L); // Giả sử có Subject với ID 2
                course3.setSemesterId(1L);
                course3.setInstructorId(2L); // Teacher với ID 8 (teacher3)
                course3.setMaxStudents(2);
                course3.setRemainSlot(2);
                course3.setCurrentStudents(0);
                courseRepository.save(course3);

                // Course 4
                Course course4 = new Course();
                course4.setCode("COURSE004");
                course4.setCourseGroup("Nhóm 01");
                course4.setPractiseGroup("Tổ TH01");
                course4.setSubjectId(4L); // Giả sử có Subject với ID 3
                course4.setSemesterId(1L);
                course4.setInstructorId(2L); // Teacher với ID 9 (teacher4)
                course4.setMaxStudents(2);
                course4.setRemainSlot(2);
                course4.setCurrentStudents(0);
                courseRepository.save(course4);



                System.out.println("Đã khởi tạo " + courseRepository.count() + " khóa học vào database");
            }
        };
    }
}