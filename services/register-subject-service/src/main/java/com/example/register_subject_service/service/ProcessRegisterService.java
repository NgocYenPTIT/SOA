package com.example.register_subject_service.service;

import com.example.register_subject_service.model.*;
import com.example.register_subject_service.util.ServiceAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Service
public class ProcessRegisterService {

    @Value("${app.global.schedule-service-url}")
    private String scheduleURL;

    @Value("${app.global.subject-service-url}")
    private String subjectURL;

    @Value("${app.global.course-service-url}")
    private String courseURL;

    @Autowired
    private  ServiceAPI serviceAPI;

    public ProcessRegisterService(ServiceAPI serviceAPI) {
        this.serviceAPI = serviceAPI;
    }

    public List<RegisterResponse> call(CourseRegistrationEvent event) {
        System.out.println("Processing registration event: " + event);

        List<RegisterResponse> messages = new ArrayList<>();
        List<RegisterResponse> messageOfValidateConflictSchedule = this.validateConflictSchedule(event);
        List<RegisterResponse> messageOfValidateEnoughCredit = this.validateEnoughCredit(event);

        if(!messageOfValidateConflictSchedule.get(0).getSuccess().equals("Success")){
            messages.addAll(messageOfValidateConflictSchedule);
        }
        if(!messageOfValidateEnoughCredit.get(0).getSuccess().equals("Success")){
            messages.addAll(messageOfValidateEnoughCredit);
        }
        //fail validate
        if (!messages.isEmpty()) return messages;

        messages.add(RegisterResponse.builder()
                .success(true)
                .status(200L)
                .message("Success")
                .build());

        return messages;
    }

    public ArrayList<RegisterResponse> validateConflictSchedule(CourseRegistrationEvent event) {
        ArrayList<RegisterResponse> messages = new ArrayList<>();
        HashMap<Long,Long> courseIdsConflictHashSet = new HashMap<>();

        // get schedule list
        ArrayList<ArrayList<Schedule>> courseSchedules = new ArrayList<>();

        for (int i = 0; i < event.getCourseIds().size(); i++) {
            Long courseId = event.getCourseIds().get(i);
            ArrayList<Schedule> schedules = (ArrayList<Schedule>) this.serviceAPI.callForList(
                    this.scheduleURL + "/schedule?courseId=" + courseId,
                    HttpMethod.GET,
                    null,
                    Schedule.class,
                    event.getToken()
            );

            courseSchedules.add(schedules);
        }
        System.out.println("Call for schedule: " + courseSchedules);
        System.out.println(courseSchedules.size());
        ObjectMapper mapper = new ObjectMapper();

        for (int i = 0; i < courseSchedules.size(); i++) {
            for (int j = i + 1; j < courseSchedules.size(); j++) {
                for (int ii = 0; ii < courseSchedules.get(i).size(); ii++) {
                    for (int jj = 0; jj < courseSchedules.get(j).size(); jj++) {
                        Schedule scheduleI = mapper.convertValue(courseSchedules.get(i).get(ii), Schedule.class);
                        Schedule scheduleJ = mapper.convertValue(courseSchedules.get(j).get(jj), Schedule.class);
                        Long startTime1 = scheduleI.getStartTime().getTime();
                        Long endTime1 = scheduleI.getEndTime().getTime();
                        Long startTime2 = scheduleJ.getStartTime().getTime();
                        Long endTime2 = scheduleJ.getEndTime().getTime();

                        if(isConflictTime(startTime1, endTime1, startTime2, endTime2)) {
                            System.out.println("conflict time here" + i + " " + j + " " + ii + " " + jj);
                        }

                        if (isConflictTime(startTime1, endTime1, startTime2, endTime2)) {
                            courseIdsConflictHashSet.put(scheduleI.getCourseId(), scheduleJ.getCourseId());
                        }
                    }
                }
            }
        }
        // have conflict
        if (!courseIdsConflictHashSet.isEmpty()) {
            System.out.println("hashsetConflict: " + courseIdsConflictHashSet);
            //call for subject
            courseIdsConflictHashSet.forEach((key, value) -> {
                Course firstCourse = (Course)this.serviceAPI.call(
                        this.courseURL + "/course/" + key,
                        HttpMethod.GET,
                        null,
                        Course.class,
                        event.getToken()
                );
                Course secondCourse = (Course)this.serviceAPI.call(
                        this.courseURL + "/course/" + value,
                        HttpMethod.GET,
                        null,
                        Course.class,
                        event.getToken()
                );
                Subject firstSubject = (Subject)this.serviceAPI.call(
                        this.subjectURL + "/subject/" + firstCourse.getSubjectId(),
                        HttpMethod.GET,
                        null,
                        Subject.class,
                        event.getToken()
                );

                Subject secondSubject = (Subject)this.serviceAPI.call(
                        this.subjectURL + "/subject/" + secondCourse.getSubjectId(),
                        HttpMethod.GET,
                        null,
                        Subject.class,
                        event.getToken()
                );
                System.out.println(firstSubject.getSubjectName());
                System.out.println(secondSubject.getSubjectName());
                messages.add(RegisterResponse.builder()
                        .success(false)
                        .status(400L)
                        .message("Môn " + firstSubject.getSubjectName().trim() + " trùng lịch với môn " + secondSubject.getSubjectName().trim())
                        .build());
            });
            return messages;
        }

        // else no conflict
        messages.add(RegisterResponse.builder()
                .success(true)
                .status(200L)
                .message("Success")
                .build());

        return messages;
    }

    public ArrayList<RegisterResponse> validateEnoughCredit(CourseRegistrationEvent event) {
        ArrayList<RegisterResponse> messages = new ArrayList<>();

        messages.add(RegisterResponse.builder()
                .success(true)
                .status(200L)
                .message("Success")
                .build());

        return messages;
    }

    public boolean isConflictTime(Long startTime1, Long endTime1, Long startTime2, Long endTime2) {
        System.out.println("is validating conflict time");
        return !(endTime1 <= startTime2 || startTime1 >= endTime2);
    }
}
