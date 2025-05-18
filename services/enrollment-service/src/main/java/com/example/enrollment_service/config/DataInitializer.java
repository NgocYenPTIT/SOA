package com.example.enrollment_service.config;

import com.example.enrollment_service.model.Enrollment;
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

                // Enrollment 1: PENDING
                Enrollment enrollment1 = new Enrollment();
                enrollment1.setCourseId(1L);
                enrollment1.setStatus("PENDING");
                enrollment1.setOrderNumber(1);

                enrollmentRepository.save(enrollment1);

                Enrollment enrollment11 = new Enrollment();
                enrollment11.setCourseId(1L);
                enrollment11.setStatus("PENDING");
                enrollment11.setOrderNumber(2);

                enrollmentRepository.save(enrollment11);

                // Enrollment 2: PENDING
                Enrollment enrollment2 = new Enrollment();
                enrollment2.setCourseId(2L);
                enrollment2.setStatus("PENDING");
                enrollment2.setOrderNumber(1);

                enrollmentRepository.save(enrollment2);

                Enrollment enrollment22 = new Enrollment();
                enrollment22.setCourseId(2L);
                enrollment22.setStatus("PENDING");
                enrollment22.setOrderNumber(2);

                enrollmentRepository.save(enrollment22);

                // Enrollment 3: PENDING
                Enrollment enrollment3 = new Enrollment();
                enrollment3.setCourseId(3L);
                enrollment3.setStatus("PENDING");
                enrollment3.setOrderNumber(1);

                enrollmentRepository.save(enrollment3);

                Enrollment enrollment33 = new Enrollment();
                enrollment33.setCourseId(3L);
                enrollment33.setStatus("PENDING");
                enrollment33.setOrderNumber(2);

                enrollmentRepository.save(enrollment33);

                // Enrollment 4: PENDING
                Enrollment enrollment4 = new Enrollment();
                enrollment4.setCourseId(4L);
                enrollment4.setStatus("PENDING");
                enrollment4.setOrderNumber(1);

                enrollmentRepository.save(enrollment4);

                Enrollment enrollment44 = new Enrollment();
                enrollment44.setCourseId(4L);
                enrollment44.setStatus("PENDING");
                enrollment44.setOrderNumber(2);

                enrollmentRepository.save(enrollment44);

                System.out.println("Đã khởi tạo " + enrollmentRepository.count() + " đăng ký môn học vào database");
            }
        };
    }
}