package com.example.wish_subject_service.config;

import com.example.wish_subject_service.model.WishSubject;
import com.example.wish_subject_service.model.WishSubject.WishSubjectStatus;
import com.example.wish_subject_service.repository.WishSubjectRepository;
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
                wishSubject1.setSemesterId(1L); // Học kỳ 2
                wishSubject1.setRegisterDate(tenDaysAgo);
                wishSubject1.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject1);

                // WishSubject 2: Student 1 đăng ký WishSubject 2 - Approved
                WishSubject wishSubject2 = new WishSubject();
                wishSubject2.setStudentId(1L);
                wishSubject2.setSubjectId(2L);
                wishSubject2.setSemesterId(1L); // Học kỳ 2
                wishSubject2.setRegisterDate(tenDaysAgo);
                wishSubject2.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject2);

                // WishSubject 3: Student 1 đăng ký WishSubject 3 - Pending
                WishSubject wishSubject3 = new WishSubject();
                wishSubject3.setStudentId(1L);
                wishSubject3.setSubjectId(3L);
                wishSubject3.setSemesterId(1L); // Học kỳ 2
                wishSubject3.setRegisterDate(eightDaysAgo);
                wishSubject3.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject3);

                // WishSubject 4: Student 2 đăng ký WishSubject 1 - Approved
                WishSubject wishSubject4 = new WishSubject();
                wishSubject4.setStudentId(1L);
                wishSubject4.setSubjectId(4L);
                wishSubject4.setSemesterId(1L); // Học kỳ 2
                wishSubject4.setRegisterDate(eightDaysAgo);
                wishSubject4.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject4);

                // WishSubject 5: Student 2 đăng ký WishSubject 4 - Rejected
                WishSubject wishSubject5 = new WishSubject();
                wishSubject5.setStudentId(2L);
                wishSubject5.setSubjectId(1L);
                wishSubject5.setSemesterId(1L); // Học kỳ 2
                wishSubject5.setRegisterDate(sixDaysAgo);
                wishSubject5.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject5);

                // WishSubject 6: Student 3 đăng ký WishSubject 2 - Approved
                WishSubject wishSubject6 = new WishSubject();
                wishSubject6.setStudentId(2L);
                wishSubject6.setSubjectId(2L);
                wishSubject6.setSemesterId(1L); // Học kỳ 2
                wishSubject6.setRegisterDate(sixDaysAgo);
                wishSubject6.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject6);

                // WishSubject 7: Student 3 đăng ký WishSubject 5 - Pending
                WishSubject wishSubject7 = new WishSubject();
                wishSubject7.setStudentId(2L);
                wishSubject7.setSubjectId(3L);
                wishSubject7.setSemesterId(1L); // Học kỳ 2
                wishSubject7.setRegisterDate(fourDaysAgo);
                wishSubject7.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject7);

                // WishSubject 8: Student 4 đăng ký WishSubject 3 - Approved
                WishSubject wishSubject8 = new WishSubject();
                wishSubject8.setStudentId(2L);
                wishSubject8.setSubjectId(4L);
                wishSubject8.setSemesterId(1L); // Học kỳ 2
                wishSubject8.setRegisterDate(fourDaysAgo);
                wishSubject8.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject8);

//////////////////////////////////////////////////////
//
///  // WishSubject 5: Student 2 đăng ký WishSubject 4 - Rejected
/// 
                WishSubject wishSubject55 = new WishSubject();
                wishSubject55.setStudentId(5L);
                wishSubject55.setSubjectId(1L);
                wishSubject55.setSemesterId(1L); // Học kỳ 2
                wishSubject55.setRegisterDate(sixDaysAgo);
                wishSubject55.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject55);

                // WishSubject 6: Student 3 đăng ký WishSubject 2 - Approved
                WishSubject wishSubject66 = new WishSubject();
                wishSubject66.setStudentId(5L);
                wishSubject66.setSubjectId(2L);
                wishSubject66.setSemesterId(1L); // Học kỳ 2
                wishSubject66.setRegisterDate(sixDaysAgo);
                wishSubject66.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject66);

                // WishSubject 7: Student 3 đăng ký WishSubject 5 - Pending
                WishSubject wishSubject77 = new WishSubject();
                wishSubject77.setStudentId(5L);
                wishSubject77.setSubjectId(3L);
                wishSubject77.setSemesterId(1L); // Học kỳ 2
                wishSubject77.setRegisterDate(fourDaysAgo);
                wishSubject77.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject77);

                // WishSubject 8: Student 4 đăng ký WishSubject 3 - Approved
                WishSubject wishSubject88 = new WishSubject();
                wishSubject88.setStudentId(5L);
                wishSubject88.setSubjectId(4L);
                wishSubject88.setSemesterId(1L); // Học kỳ 2
                wishSubject88.setRegisterDate(fourDaysAgo);
                wishSubject88.setStatus(WishSubjectStatus.APPROVED);
                wishSubjectRepository.save(wishSubject88);
                


                System.out.println("Đã khởi tạo " + wishSubjectRepository.count() + " môn nguyện vọng vào database");
            }
        };
    }
}