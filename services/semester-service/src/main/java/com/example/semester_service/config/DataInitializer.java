package com.example.semester_service.config;

import com.example.semester_service.model.Semester;
import com.example.semester_service.model.Semester.SemesterStatus;
import com.example.semester_service.repository.SemesterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Configuration
public class DataInitializer {

    @Bean
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

                calendar.set(2024, Calendar.DECEMBER, 31); // 31/12/2024
                Date endDate1 = calendar.getTime();

                calendar.set(2024, Calendar.JULY, 15); // 15/07/2024
                Date regStartDate1 = calendar.getTime();

                calendar.set(2024, Calendar.AUGUST, 5); // 05/08/2024
                Date regEndDate1 = calendar.getTime();

                // Học kỳ 2 - 2024-2025
                calendar.set(2025, Calendar.JANUARY, 15); // 15/01/2025
                Date startDate2 = calendar.getTime();

                calendar.set(2025, Calendar.MAY, 31); // 31/05/2025
                Date endDate2 = calendar.getTime();

                calendar.set(2024, Calendar.DECEMBER, 15); // 15/12/2024
                Date regStartDate2 = calendar.getTime();

                calendar.set(2025, Calendar.JANUARY, 5); // 05/01/2025
                Date regEndDate2 = calendar.getTime();

                // Học kỳ hè - 2024-2025
                calendar.set(2025, Calendar.JUNE, 15); // 15/06/2025
                Date startDate3 = calendar.getTime();

                calendar.set(2025, Calendar.AUGUST, 15); // 15/08/2025
                Date endDate3 = calendar.getTime();

                calendar.set(2025, Calendar.MAY, 15); // 15/05/2025
                Date regStartDate3 = calendar.getTime();

                calendar.set(2025, Calendar.JUNE, 5); // 05/06/2025
                Date regEndDate3 = calendar.getTime();

                // Semester 1 - Học kỳ 1 năm 2024-2025 - CNTT
                Semester semester1 = new Semester();
                semester1.setSemesterCode("2024-2025-1-CNTT");
                semester1.setSemesterName("Học kỳ 1");
                semester1.setMajorId(1L); // Công nghệ thông tin
                semester1.setAcademicYear("2024-2025");
                semester1.setStartDate(startDate1);
                semester1.setEndDate(endDate1);
                semester1.setRegistrationStartDate(regStartDate1);
                semester1.setRegistrationEndDate(regEndDate1);
                semester1.setStatus(SemesterStatus.COMPLETED);
                semesterRepository.save(semester1);

                // Semester 2 - Học kỳ 2 năm 2024-2025 - CNTT
                Semester semester2 = new Semester();
                semester2.setSemesterCode("2024-2025-2-CNTT");
                semester2.setSemesterName("Học kỳ 2");
                semester2.setMajorId(1L); // Công nghệ thông tin
                semester2.setAcademicYear("2024-2025");
                semester2.setStartDate(startDate2);
                semester2.setEndDate(endDate2);
                semester2.setRegistrationStartDate(regStartDate2);
                semester2.setRegistrationEndDate(regEndDate2);
                semester2.setStatus(SemesterStatus.ONGOING);
                semesterRepository.save(semester2);

                // Semester 3 - Học kỳ hè năm 2024-2025 - CNTT
                Semester semester3 = new Semester();
                semester3.setSemesterCode("2024-2025-3-CNTT");
                semester3.setSemesterName("Học kỳ hè");
                semester3.setMajorId(1L); // Công nghệ thông tin
                semester3.setAcademicYear("2024-2025");
                semester3.setStartDate(startDate3);
                semester3.setEndDate(endDate3);
                semester3.setRegistrationStartDate(regStartDate3);
                semester3.setRegistrationEndDate(regEndDate3);
                semester3.setStatus(SemesterStatus.UPCOMING);
                semesterRepository.save(semester3);

                // Semester 4 - Học kỳ 1 năm 2024-2025 - KTPM
                Semester semester4 = new Semester();
                semester4.setSemesterCode("2024-2025-1-KTPM");
                semester4.setSemesterName("Học kỳ 1");
                semester4.setMajorId(2L); // Kỹ thuật phần mềm
                semester4.setAcademicYear("2024-2025");
                semester4.setStartDate(startDate1);
                semester4.setEndDate(endDate1);
                semester4.setRegistrationStartDate(regStartDate1);
                semester4.setRegistrationEndDate(regEndDate1);
                semester4.setStatus(SemesterStatus.COMPLETED);
                semesterRepository.save(semester4);

                // Semester 5 - Học kỳ 2 năm 2024-2025 - KTPM
                Semester semester5 = new Semester();
                semester5.setSemesterCode("2024-2025-2-KTPM");
                semester5.setSemesterName("Học kỳ 2");
                semester5.setMajorId(2L); // Kỹ thuật phần mềm
                semester5.setAcademicYear("2024-2025");
                semester5.setStartDate(startDate2);
                semester5.setEndDate(endDate2);
                semester5.setRegistrationStartDate(regStartDate2);
                semester5.setRegistrationEndDate(regEndDate2);
                semester5.setStatus(SemesterStatus.ONGOING);
                semesterRepository.save(semester5);

                System.out.println("Đã khởi tạo " + semesterRepository.count() + " học kỳ vào database");
            }
        };
    }
}