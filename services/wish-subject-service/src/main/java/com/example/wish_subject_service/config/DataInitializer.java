package com.example.wish_subject_service.config;

import com.example.wish_subject_service.model.WishSubject;
import com.example.wish_subject_service.model.WishSubject.WishSubjectStatus;
import com.example.wish_subject_service.repository.WishSubjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(WishSubjectRepository wishSubjectRepository) {
        return args -> {
            // Khởi tạo môn nguyện vọng (WishSubjects)
            if (wishSubjectRepository.count() == 0) {
                System.out.println("Bắt đầu khởi tạo dữ liệu môn nguyện vọng...");

                // Lấy ngày hiện tại
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();

                // Quay lại 10 ngày
                calendar.add(Calendar.DAY_OF_MONTH, -10);
                Date tenDaysAgo = calendar.getTime();

                // Quay lại 8 ngày
                calendar.add(Calendar.DAY_OF_MONTH, 2);
                Date eightDaysAgo = calendar.getTime();

                // Quay lại 6 ngày
                calendar.add(Calendar.DAY_OF_MONTH, 2);
                Date sixDaysAgo = calendar.getTime();

                // Quay lại 4 ngày
                calendar.add(Calendar.DAY_OF_MONTH, 2);
                Date fourDaysAgo = calendar.getTime();

                // Quay lại 2 ngày
                calendar.add(Calendar.DAY_OF_MONTH, 2);
                Date twoDaysAgo = calendar.getTime();

                // WishSubject 1: Student 1 đăng ký WishSubject 1 - Approved
                WishSubject wishSubject1 = new WishSubject();
                wishSubject1.setStudentId(1L);
                wishSubject1.setSubjectId(1L);
                wishSubject1.setSemesterId(2L); // Học kỳ 2
                wishSubject1.setRegisterDate(tenDaysAgo);
                wishSubject1.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject1);

                // WishSubject 2: Student 1 đăng ký WishSubject 2 - Approved
                WishSubject wishSubject2 = new WishSubject();
                wishSubject2.setStudentId(1L);
                wishSubject2.setSubjectId(2L);
                wishSubject2.setSemesterId(2L); // Học kỳ 2
                wishSubject2.setRegisterDate(tenDaysAgo);
                wishSubject2.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject2);

                // WishSubject 3: Student 1 đăng ký WishSubject 3 - Pending
                WishSubject wishSubject3 = new WishSubject();
                wishSubject3.setStudentId(1L);
                wishSubject3.setSubjectId(3L);
                wishSubject3.setSemesterId(2L); // Học kỳ 2
                wishSubject3.setRegisterDate(eightDaysAgo);
                wishSubject3.setStatus(WishSubjectStatus.PENDING);
                wishSubjectRepository.save(wishSubject3);

                // WishSubject 4: Student 2 đăng ký WishSubject 1 - Approved
                WishSubject wishSubject4 = new WishSubject();
                wishSubject4.setStudentId(2L);
                wishSubject4.setSubjectId(1L);
                wishSubject4.setSemesterId(2L); // Học kỳ 2
                wishSubject4.setRegisterDate(eightDaysAgo);
                wishSubject4.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject4);

                // WishSubject 5: Student 2 đăng ký WishSubject 4 - Rejected
                WishSubject wishSubject5 = new WishSubject();
                wishSubject5.setStudentId(2L);
                wishSubject5.setSubjectId(4L);
                wishSubject5.setSemesterId(2L); // Học kỳ 2
                wishSubject5.setRegisterDate(sixDaysAgo);
                wishSubject5.setStatus(WishSubjectStatus.REJECTED);
                wishSubjectRepository.save(wishSubject5);

                // WishSubject 6: Student 3 đăng ký WishSubject 2 - Approved
                WishSubject wishSubject6 = new WishSubject();
                wishSubject6.setStudentId(3L);
                wishSubject6.setSubjectId(2L);
                wishSubject6.setSemesterId(2L); // Học kỳ 2
                wishSubject6.setRegisterDate(sixDaysAgo);
                wishSubject6.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject6);

                // WishSubject 7: Student 3 đăng ký WishSubject 5 - Pending
                WishSubject wishSubject7 = new WishSubject();
                wishSubject7.setStudentId(3L);
                wishSubject7.setSubjectId(5L);
                wishSubject7.setSemesterId(2L); // Học kỳ 2
                wishSubject7.setRegisterDate(fourDaysAgo);
                wishSubject7.setStatus(WishSubjectStatus.PENDING);
                wishSubjectRepository.save(wishSubject7);

                // WishSubject 8: Student 4 đăng ký WishSubject 3 - Approved
                WishSubject wishSubject8 = new WishSubject();
                wishSubject8.setStudentId(4L);
                wishSubject8.setSubjectId(3L);
                wishSubject8.setSemesterId(2L); // Học kỳ 2
                wishSubject8.setRegisterDate(fourDaysAgo);
                wishSubject8.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject8);

                // WishSubject 9: Student 5 đăng ký WishSubject 4 - Rejected
                WishSubject wishSubject9 = new WishSubject();
                wishSubject9.setStudentId(5L);
                wishSubject9.setSubjectId(4L);
                wishSubject9.setSemesterId(2L); // Học kỳ 2
                wishSubject9.setRegisterDate(twoDaysAgo);
                wishSubject9.setStatus(WishSubjectStatus.REJECTED);
                wishSubjectRepository.save(wishSubject9);

                // WishSubject 10: Student 5 đăng ký WishSubject 5 - Pending
                WishSubject wishSubject10 = new WishSubject();
                wishSubject10.setStudentId(5L);
                wishSubject10.setSubjectId(5L);
                wishSubject10.setSemesterId(2L); // Học kỳ 2
                wishSubject10.setRegisterDate(twoDaysAgo);
                wishSubject10.setStatus(WishSubjectStatus.PENDING);
                wishSubjectRepository.save(wishSubject10);

                System.out.println("Đã khởi tạo " + wishSubjectRepository.count() + " môn nguyện vọng vào database");
            }
        };
    }
}