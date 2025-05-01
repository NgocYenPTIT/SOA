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
                course1.setInstructorId(6L); // Teacher với ID 6 (teacher1)
                course1.setMaxStudents(40);
                course1.setRemainSlot(35);
                course1.setCurrentStudents(5);
                courseRepository.save(course1);

                // Course 2
                Course course2 = new Course();
                course2.setCode("COURSE002");
                course2.setCourseGroup("Nhóm 02");
                course2.setPractiseGroup("Tổ TH02");
                course2.setSubjectId(1L);
                course2.setSemesterId(1L);
                course2.setInstructorId(7L); // Teacher với ID 7 (teacher2)
                course2.setMaxStudents(35);
                course2.setRemainSlot(30);
                course2.setCurrentStudents(5);
                courseRepository.save(course2);

                // Course 3
                Course course3 = new Course();
                course3.setCode("COURSE003");
                course3.setCourseGroup("Nhóm 01");
                course3.setPractiseGroup("Tổ TH01");
                course3.setSubjectId(2L); // Giả sử có Subject với ID 2
                course3.setSemesterId(1L);
                course3.setInstructorId(8L); // Teacher với ID 8 (teacher3)
                course3.setMaxStudents(45);
                course3.setRemainSlot(42);
                course3.setCurrentStudents(3);
                courseRepository.save(course3);

                // Course 4
                Course course4 = new Course();
                course4.setCode("COURSE004");
                course4.setCourseGroup("Nhóm 01");
                course4.setPractiseGroup("Tổ TH01");
                course4.setSubjectId(3L); // Giả sử có Subject với ID 3
                course4.setSemesterId(1L);
                course4.setInstructorId(9L); // Teacher với ID 9 (teacher4)
                course4.setMaxStudents(30);
                course4.setRemainSlot(28);
                course4.setCurrentStudents(2);
                courseRepository.save(course4);

                // Course 5
                Course course5 = new Course();
                course5.setCode("COURSE005");
                course5.setCourseGroup("Nhóm 02");
                course5.setPractiseGroup("Tổ TH02");
                course5.setSubjectId(3L);
                course5.setSemesterId(1L);
                course5.setInstructorId(10L); // Teacher với ID 10 (teacher5)
                course5.setMaxStudents(35);
                course5.setRemainSlot(34);
                course5.setCurrentStudents(1);
                courseRepository.save(course5);

                // Course 6
                Course course6 = new Course();
                course6.setCode("COURSE006");
                course6.setCourseGroup("Nhóm 01");
                course6.setPractiseGroup("Tổ TH01");
                course6.setSubjectId(4L);
                course6.setSemesterId(2L);
                course6.setInstructorId(6L);
                course6.setMaxStudents(40);
                course6.setRemainSlot(37);
                course6.setCurrentStudents(3);
                courseRepository.save(course6);

                // Course 7
                Course course7 = new Course();
                course7.setCode("COURSE007");
                course7.setCourseGroup("Nhóm 02");
                course7.setPractiseGroup("Tổ TH02");
                course7.setSubjectId(4L);
                course7.setSemesterId(2L);
                course7.setInstructorId(7L);
                course7.setMaxStudents(40);
                course7.setRemainSlot(38);
                course7.setCurrentStudents(2);
                courseRepository.save(course7);

                // Course 8
                Course course8 = new Course();
                course8.setCode("COURSE008");
                course8.setCourseGroup("Nhóm 01");
                course8.setPractiseGroup("Tổ TH01");
                course8.setSubjectId(5L);
                course8.setSemesterId(2L);
                course8.setInstructorId(8L);
                course8.setMaxStudents(35);
                course8.setRemainSlot(33);
                course8.setCurrentStudents(2);
                courseRepository.save(course8);

                // Course 9
                Course course9 = new Course();
                course9.setCode("COURSE009");
                course9.setCourseGroup("Nhóm 01");
                course9.setPractiseGroup("Tổ TH01");
                course9.setSubjectId(6L);
                course9.setSemesterId(2L);
                course9.setInstructorId(9L);
                course9.setMaxStudents(45);
                course9.setRemainSlot(41);
                course9.setCurrentStudents(4);
                courseRepository.save(course9);

                // Course 10
                Course course10 = new Course();
                course10.setCode("COURSE010");
                course10.setCourseGroup("Nhóm 02");
                course10.setPractiseGroup("Tổ TH02");
                course10.setSubjectId(6L);
                course10.setSemesterId(2L);
                course10.setInstructorId(10L);
                course10.setMaxStudents(45);
                course10.setRemainSlot(43);
                course10.setCurrentStudents(2);
                courseRepository.save(course10);

                System.out.println("Đã khởi tạo " + courseRepository.count() + " khóa học vào database");
            }
        };
    }
}