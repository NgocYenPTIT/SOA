package com.example.semester_service.config;

import com.example.semester_service.model.Semester;
import com.example.semester_service.model.Semester.SemesterStatus;
import com.example.semester_service.repository.SemesterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initData(SemesterRepository semesterRepository) {
        return args -> {
            // Khởi tạo học kỳ (Semesters)
            if (semesterRepository.count() == 0) {
                System.out.println("Bắt đầu khởi tạo dữ liệu học kỳ...");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                // Tạo các ngày
                Calendar calendar = Calendar.getInstance();

                // Học kỳ 1 - 2024-2025
                calendar.set(2024, Calendar.AUGUST, 15); // 15/08/2024
                Date startDate1 = calendar.getTime();

                calendar.set(2030, Calendar.DECEMBER, 31); // 31/12/2024
                Date endDate1 = calendar.getTime();

                calendar.set(2024, Calendar.JULY, 15); // 15/07/2024
                Date regStartDate1 = calendar.getTime();

                calendar.set(2030, Calendar.AUGUST, 5); // 05/08/2024
                Date regEndDate1 = calendar.getTime();

                // Semester 1 - Học kỳ 1 năm 2024-2025 - CNTT
                Semester semester1 = new Semester();
                semester1.setSemesterCode("2024-2025");
                semester1.setSemesterName("Học kỳ 1");
                semester1.setMajorId(1L); // Công nghệ thông tin
                semester1.setAcademicYear("2024-2025");
                semester1.setStartDate(startDate1);
                semester1.setEndDate(endDate1);
                semester1.setRegistrationStartDate(regStartDate1);
                semester1.setRegistrationEndDate(regEndDate1);
                semester1.setStatus(SemesterStatus.ONGOING);
                semesterRepository.save(semester1);


                System.out.println("Đã khởi tạo " + semesterRepository.count() + " học kỳ vào database");
            }
        };
    }
}