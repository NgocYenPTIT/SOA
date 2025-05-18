package com.example.schedule_service.config;

import com.example.schedule_service.model.Schedule;
import com.example.schedule_service.repository.ScheduleRepository;
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
    public CommandLineRunner initData(ScheduleRepository scheduleRepository) {
        return args -> {
            // Khởi tạo lịch học (Schedules)
            if (scheduleRepository.count() == 0) {
                System.out.println("Bắt đầu khởi tạo dữ liệu lịch học...");

                // Tạo các ngày và giờ học
                Calendar calendar = Calendar.getInstance();
                // Thứ 2
                calendar.set(2025, Calendar.MAY, 5, 7, 30, 0); // 7:30 AM, 5/5/2025
                Date startTime1 = calendar.getTime();

                calendar.set(2025, Calendar.MAY, 5, 10, 0, 0); // 10:00 AM, 5/5/2025
                Date endTime1 = calendar.getTime();

                // Thứ 2 chiều
                calendar.set(2025, Calendar.MAY, 5, 13, 30, 0); // 1:30 PM, 5/5/2025
                Date startTime2 = calendar.getTime();

                calendar.set(2025, Calendar.MAY, 5, 16, 0, 0); // 4:00 PM, 5/5/2025
                Date endTime2 = calendar.getTime();

                // Thứ 3
                calendar.set(2025, Calendar.MAY, 6, 7, 30, 0); // 7:30 AM, 6/5/2025
                Date startTime3 = calendar.getTime();

                calendar.set(2025, Calendar.MAY, 6, 10, 0, 0); // 10:00 AM, 6/5/2025
                Date endTime3 = calendar.getTime();

                // Thứ 3 chiều
                calendar.set(2025, Calendar.MAY, 6, 13, 30, 0); // 1:30 PM, 6/5/2025
                Date startTime4 = calendar.getTime();

                calendar.set(2025, Calendar.MAY, 6, 16, 0, 0); // 4:00 PM, 6/5/2025
                Date endTime4 = calendar.getTime();

                // Thứ 4
                calendar.set(2025, Calendar.MAY, 7, 7, 30, 0); // 7:30 AM, 7/5/2025
                Date startTime5 = calendar.getTime();

                calendar.set(2025, Calendar.MAY, 7, 10, 0, 0); // 10:00 AM, 7/5/2025
                Date endTime5 = calendar.getTime();

                // Thứ 4 chiều
                calendar.set(2025, Calendar.MAY, 7, 13, 30, 0); // 1:30 PM, 7/5/2025
                Date startTime6 = calendar.getTime();

                calendar.set(2025, Calendar.MAY, 7, 16, 0, 0); // 4:00 PM, 7/5/2025
                Date endTime6 = calendar.getTime();

                // Thứ 5
                calendar.set(2025, Calendar.MAY, 8, 7, 30, 0); // 7:30 AM, 8/5/2025
                Date startTime7 = calendar.getTime();

                calendar.set(2025, Calendar.MAY, 8, 10, 0, 0); // 10:00 AM, 8/5/2025
                Date endTime7 = calendar.getTime();

                // Thứ 5 chiều
                calendar.set(2025, Calendar.MAY, 8, 13, 30, 0); // 1:30 PM, 8/5/2025
                Date startTime8 = calendar.getTime();

                calendar.set(2025, Calendar.MAY, 8, 16, 0, 0); // 4:00 PM, 8/5/2025
                Date endTime8 = calendar.getTime();

                // Thứ 6
                calendar.set(2025, Calendar.MAY, 9, 7, 30, 0); // 7:30 AM, 9/5/2025
                Date startTime9 = calendar.getTime();

                calendar.set(2025, Calendar.MAY, 9, 10, 0, 0); // 10:00 AM, 9/5/2025
                Date endTime9 = calendar.getTime();

                // Thứ 6 chiều
                calendar.set(2025, Calendar.MAY, 9, 13, 30, 0); // 1:30 PM, 9/5/2025
                Date startTime10 = calendar.getTime();

                calendar.set(2025, Calendar.MAY, 9, 16, 0, 0); // 4:00 PM, 9/5/2025
                Date endTime10 = calendar.getTime();

                // Schedule 1 - Thứ 2 sáng - Môn 1
                Schedule schedule1 = new Schedule();
                schedule1.setCourseId(1L);
                schedule1.setRoomId(1L);
                schedule1.setTeacherId(3L);
                schedule1.setStartTime(startTime1);
                schedule1.setEndTime(endTime1);
                schedule1.setType("LÝ THUYẾT");
                scheduleRepository.save(schedule1);

                // Schedule 2 - Thứ 2 chiều - Môn 1
                Schedule schedule2 = new Schedule();
                schedule2.setCourseId(1L);
                schedule2.setRoomId(1L); // Phòng lab
                schedule2.setTeacherId(3L);
                schedule2.setStartTime(startTime2);
                schedule2.setEndTime(endTime2);
                schedule2.setType("LÝ THUYẾT");
                scheduleRepository.save(schedule2);

                // Schedule 3 - Thứ 3 sáng - Môn 2
                Schedule schedule3 = new Schedule();
                schedule3.setCourseId(2L);
                schedule3.setRoomId(2L);
                schedule3.setTeacherId(4L);
                schedule3.setStartTime(startTime1);//conflict
                schedule3.setEndTime(endTime1);//conflict
                schedule3.setType("LÝ THUYẾT");
                scheduleRepository.save(schedule3);

                // Schedule 4 - Thứ 3 chiều - Môn 2
                Schedule schedule4 = new Schedule();
                schedule4.setCourseId(2L);
                schedule4.setRoomId(2L); // Phòng lab
                schedule4.setTeacherId(4L);
                schedule4.setStartTime(startTime4);
                schedule4.setEndTime(endTime4);
                schedule4.setType("LÝ THUYẾT");
                scheduleRepository.save(schedule4);

                // Schedule 5 - Thứ 4 sáng - Môn 3
                Schedule schedule5 = new Schedule();
                schedule5.setCourseId(3L);
                schedule5.setRoomId(3L);
                schedule5.setTeacherId(4L);
                schedule5.setStartTime(startTime5);
                schedule5.setEndTime(endTime5);
                schedule5.setType("LÝ THUYẾT");
                scheduleRepository.save(schedule5);

                // Schedule 6 - Thứ 4 chiều - Môn 3
                Schedule schedule6 = new Schedule();
                schedule6.setCourseId(3L);
                schedule6.setRoomId(3L); // Phòng lab
                schedule6.setTeacherId(4L);
                schedule6.setStartTime(startTime6);
                schedule6.setEndTime(endTime6);
                schedule6.setType("LÝ THUYẾT");
                scheduleRepository.save(schedule6);

                // Schedule 7 - Thứ 5 sáng - Môn 4
                Schedule schedule7 = new Schedule();
                schedule7.setCourseId(4L);
                schedule7.setRoomId(4L);
                schedule7.setTeacherId(4L);
                schedule7.setStartTime(startTime7);
                schedule7.setEndTime(endTime7);
                schedule7.setType("LÝ THUYẾT");
                scheduleRepository.save(schedule7);

                // Schedule 8 - Thứ 5 chiều - Môn 4
                Schedule schedule8 = new Schedule();
                schedule8.setCourseId(4L);
                schedule8.setRoomId(4L); // Phòng lab
                schedule8.setTeacherId(4L);
                schedule8.setStartTime(startTime8);
                schedule8.setEndTime(endTime8);
                schedule8.setType("LÝ THUYẾT");
                scheduleRepository.save(schedule8);


                System.out.println("Đã khởi tạo " + scheduleRepository.count() + " lịch học vào database");
            }
        };
    }
}