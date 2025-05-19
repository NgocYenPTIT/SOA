package com.example.read_model.config;

import com.example.read_model.model.OpeningSubject;
import com.example.read_model.model.RegisterSubjectView;
import com.example.read_model.model.RegisteredSubject;
import com.example.read_model.model.ScheduleResponse;
import com.example.read_model.repository.RegisterSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.read_model.repository")
public class DataInitializer {

    @Autowired
    private RegisterSubjectRepository registerSubjectViewRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Kiểm tra xem dữ liệu đã tồn tại chưa
            if (registerSubjectViewRepository.count() > 0) {
                System.out.println("Dữ liệu đã tồn tại, bỏ qua quá trình khởi tạo.");
                return;
            }

            // Tạo các đối tượng Schedule
            ScheduleResponse scheduleResponse1 = ScheduleResponse.builder()
                    .id(1L)
                    .content("Thứ 2 (7:00-9:00) - Phòng A101")
                    .build();

            ScheduleResponse scheduleResponse2 = ScheduleResponse.builder()
                    .id(2L)
                    .content("Thứ 4 (13:00-15:00) - Phòng B202")
                    .build();

            ScheduleResponse scheduleResponse3 = ScheduleResponse.builder()
                    .id(3L)
                    .content("Thứ 6 (9:00-11:00) - Phòng C303")
                    .build();

            // Tạo các môn học mở
            OpeningSubject subject1 = OpeningSubject.builder()
                    .subjectId(1001L)
                    .subjectName("Lập trình Java")
                    .subjectCode("IT1101")
                    .courseCode("IT1101_01")
                    .courseGroup("L01")
                    .practiseGroup("P01")
                    .amountOfCredit(3)
                    .remainSlot(25)
                    .maxStudent(40)
                    .scheduleResponses(Arrays.asList(scheduleResponse1))
                    .build();

            OpeningSubject subject2 = OpeningSubject.builder()
                    .subjectId(1002L)
                    .subjectName("Cơ sở dữ liệu")
                    .subjectCode("IT1102")
                    .courseCode("IT1102_01")
                    .courseGroup("L01")
                    .practiseGroup("P01")
                    .amountOfCredit(4)
                    .remainSlot(15)
                    .maxStudent(35)
                    .scheduleResponses(Arrays.asList(scheduleResponse2))
                    .build();

            OpeningSubject subject3 = OpeningSubject.builder()
                    .subjectId(1003L)
                    .subjectName("Kỹ thuật lập trình")
                    .subjectCode("IT1103")
                    .courseCode("IT1103_01")
                    .courseGroup("L01")
                    .practiseGroup("P01")
                    .amountOfCredit(3)
                    .remainSlot(20)
                    .maxStudent(30)
                    .scheduleResponses(Arrays.asList(scheduleResponse3))
                    .build();

            // Tạo 1 môn học đã đăng ký
            RegisteredSubject registeredSubject = RegisteredSubject.builder()
                    .subjectId(1001L)
                    .subjectName("Lập trình Java")
                    .subjectCode("IT1101")
                    .courseCode("IT1101_01")
                    .courseGroup("L01")
                    .practiseGroup("P01")
                    .enrollmentDate("2024-05-18")
                    .Status("Đã đăng ký")
                    .scheduleResponses(Arrays.asList(scheduleResponse1))
                    .build();

            // Thiết lập ngày kết thúc đăng ký (7 ngày kể từ hôm nay)
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            Date endDate = calendar.getTime();

            // Tạo thông tin đăng ký học phần cho 1 sinh viên
            RegisterSubjectView studentView = RegisterSubjectView.builder()
                    .studentId(100L)
                    .semester("HK1")
                    .year("2024-2025")
                    .openSubject(Arrays.asList(subject1, subject2, subject3))
                    .numOfRegisteredSubject(1)
                    .numOfRegisteredCredit(3)
                    .registeredSubject(Arrays.asList(registeredSubject))
                    .build();

            // Lưu vào database
            registerSubjectViewRepository.save(studentView);

            System.out.println("Đã khởi tạo dữ liệu mẫu cho sinh viên với ID: 20210001");
        };
    }
}