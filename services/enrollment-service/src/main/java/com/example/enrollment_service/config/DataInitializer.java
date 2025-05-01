package com.example.enrollment_service.config;

import com.example.enrollment_service.model.Enrollment;
import com.example.enrollment_service.model.Enrollment.EnrollmentStatus;
import com.example.enrollment_service.repository.EnrollmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(EnrollmentRepository enrollmentRepository) {
        return args -> {
            // Khởi tạo đăng ký môn học (Enrollments)
            if (enrollmentRepository.count() == 0) {
                System.out.println("Bắt đầu khởi tạo dữ liệu đăng ký môn học...");

                // Lấy ngày hiện tại
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();

                // Quay lại 5 ngày
                calendar.add(Calendar.DAY_OF_MONTH, -5);
                Date fiveDaysAgo = calendar.getTime();

                // Quay lại 4 ngày
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Date fourDaysAgo = calendar.getTime();

                // Quay lại 3 ngày
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Date threeDaysAgo = calendar.getTime();

                // Quay lại 2 ngày
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Date twoDaysAgo = calendar.getTime();

                // Quay lại 1 ngày
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Date oneDayAgo = calendar.getTime();

                // Enrollment 1: Student 1 đăng ký Course 1 - Approved
                Enrollment enrollment1 = new Enrollment();
                enrollment1.setStudentId(1L);
                enrollment1.setCourseId(1L);
                enrollment1.setEnrollmentDate(fiveDaysAgo);
                enrollment1.setStatus(EnrollmentStatus.APPROVED);
                enrollmentRepository.save(enrollment1);

                // Enrollment 2: Student 1 đăng ký Course 2 - Approved
                Enrollment enrollment2 = new Enrollment();
                enrollment2.setStudentId(1L);
                enrollment2.setCourseId(2L);
                enrollment2.setEnrollmentDate(fiveDaysAgo);
                enrollment2.setStatus(EnrollmentStatus.APPROVED);
                enrollmentRepository.save(enrollment2);

                // Enrollment 3: Student 2 đăng ký Course 1 - Approved
                Enrollment enrollment3 = new Enrollment();
                enrollment3.setStudentId(2L);
                enrollment3.setCourseId(1L);
                enrollment3.setEnrollmentDate(fourDaysAgo);
                enrollment3.setStatus(EnrollmentStatus.APPROVED);
                enrollmentRepository.save(enrollment3);

                // Enrollment 4: Student 2 đăng ký Course 3 - Approved
                Enrollment enrollment4 = new Enrollment();
                enrollment4.setStudentId(2L);
                enrollment4.setCourseId(3L);
                enrollment4.setEnrollmentDate(fourDaysAgo);
                enrollment4.setStatus(EnrollmentStatus.APPROVED);
                enrollmentRepository.save(enrollment4);

                // Enrollment 5: Student 3 đăng ký Course 2 - Approved
                Enrollment enrollment5 = new Enrollment();
                enrollment5.setStudentId(3L);
                enrollment5.setCourseId(2L);
                enrollment5.setEnrollmentDate(threeDaysAgo);
                enrollment5.setStatus(EnrollmentStatus.APPROVED);
                enrollmentRepository.save(enrollment5);

                // Enrollment 6: Student 3 đăng ký Course 4 - Rejected
                Enrollment enrollment6 = new Enrollment();
                enrollment6.setStudentId(3L);
                enrollment6.setCourseId(4L);
                enrollment6.setEnrollmentDate(threeDaysAgo);
                enrollment6.setStatus(EnrollmentStatus.REJECTED);
                enrollmentRepository.save(enrollment6);

                // Enrollment 7: Student 4 đăng ký Course 3 - Approved
                Enrollment enrollment7 = new Enrollment();
                enrollment7.setStudentId(4L);
                enrollment7.setCourseId(3L);
                enrollment7.setEnrollmentDate(twoDaysAgo);
                enrollment7.setStatus(EnrollmentStatus.APPROVED);
                enrollmentRepository.save(enrollment7);

                // Enrollment 8: Student 4 đăng ký Course 5 - Pending
                Enrollment enrollment8 = new Enrollment();
                enrollment8.setStudentId(4L);
                enrollment8.setCourseId(5L);
                enrollment8.setEnrollmentDate(twoDaysAgo);
                enrollment8.setStatus(EnrollmentStatus.PENDING);
                enrollmentRepository.save(enrollment8);

                // Enrollment 9: Student 5 đăng ký Course 4 - Approved
                Enrollment enrollment9 = new Enrollment();
                enrollment9.setStudentId(5L);
                enrollment9.setCourseId(4L);
                enrollment9.setEnrollmentDate(oneDayAgo);
                enrollment9.setStatus(EnrollmentStatus.APPROVED);
                enrollmentRepository.save(enrollment9);

                // Enrollment 10: Student 5 đăng ký Course 5 - Pending
                Enrollment enrollment10 = new Enrollment();
                enrollment10.setStudentId(5L);
                enrollment10.setCourseId(5L);
                enrollment10.setEnrollmentDate(oneDayAgo);
                enrollment10.setStatus(EnrollmentStatus.PENDING);
                enrollmentRepository.save(enrollment10);

                System.out.println("Đã khởi tạo " + enrollmentRepository.count() + " đăng ký môn học vào database");
            }
        };
    }
}