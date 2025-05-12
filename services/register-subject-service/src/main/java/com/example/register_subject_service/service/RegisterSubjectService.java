package com.example.register_subject_service.service;

import com.example.register_subject_service.model.RegisterResponse;
import com.example.register_subject_service.model.RegisterSubjectDto;
import com.example.register_subject_service.model.Schedule;
import com.example.register_subject_service.util.ServiceAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class RegisterSubjectService {

    @Value("${app.global.schedule-service-url}")
    private String scheduleURL;

    private final ServiceAPI serviceAPI;

    public RegisterSubjectService(ServiceAPI serviceAPI) {
        this.serviceAPI = serviceAPI;
    }


    public List<RegisterResponse> registerSubject(HttpServletRequest request, @RequestBody RegisterSubjectDto form) {
        // emit event and send data attach to processing
        //        List<RegisterResponse> messages = new ArrayList<>();
//
//        messages.addAll(this.validateConflictSchedule(request, form));
//        messages.addAll(this.validateEnoughCredit(request, form));
//
//        //fail validate
//        if (!messages.isEmpty()) return messages;
//
//        //success
//        this.save(request, form);
//
//        messages.add(RegisterResponse.builder()
//                .success(true)
//                .status(200L)
//                .message("Success")
//                .build());
//
//        return messages;
        //return status PROCESSING...
        return Collections.singletonList(RegisterResponse.builder().success(true).status(202L).message("Processing...").build());
    }

    public void save(HttpServletRequest request, @RequestBody RegisterSubjectDto form) {
        // squash validateEnoughSlot();
        // TODO:emit event
    }

    public ArrayList<RegisterResponse> validateConflictSchedule(HttpServletRequest request, @RequestBody RegisterSubjectDto form) {
        ArrayList<RegisterResponse> messages = new ArrayList<>();
        // get schedule list
        ArrayList<ArrayList<Schedule>> courseSchedules = new ArrayList<>();

        for (int i = 0; i < form.getCourseIds().size(); i++) {
            Long courseId = form.getCourseIds().get(i);
            ArrayList<Schedule> schedules = (ArrayList<Schedule>) this.serviceAPI.callForList(
                    this.scheduleURL + "/schedule?courseId=" + courseId,
                    HttpMethod.GET,
                    null,
                    Schedule.class,
                    (String) request.getAttribute("token")
            );

            courseSchedules.add(schedules);
        }

        ObjectMapper mapper = new ObjectMapper();

        for (int i = 0; i < courseSchedules.size(); i++) {
            for (int j = i + 1; j < courseSchedules.size(); j++) {
                for(int ii = 0 ; ii < courseSchedules.get(i).size(); ii++){
                    for(int jj = 0 ; jj < courseSchedules.get(j).size(); jj++){
                        Schedule scheduleI = mapper.convertValue(courseSchedules.get(i).get(ii), Schedule.class);
                        Schedule scheduleJ = mapper.convertValue(courseSchedules.get(j).get(jj), Schedule.class);
                        Long startTime1 = scheduleI.getStartTime().getTime();
                        Long endTime1 = scheduleI.getEndTime().getTime();
                        Long startTime2 = scheduleJ.getStartTime().getTime();
                        Long endTime2 = scheduleJ.getEndTime().getTime();

                        if(isConflictTime(startTime1,endTime1,startTime2,endTime2)){
                            messages.add(RegisterResponse.builder()
                                    .success(false)
                                    .status(400L)
                                    .message("Môn " + "//TODO" + "trùng lịch với môn " + "//TODO")
                                    .build());
                        }

                    }
                }
            }
        }
        // have conflict
        if(!messages.isEmpty()) return messages;

        // else no conflict
        messages.add(RegisterResponse.builder()
                .success(true)
                .status(200L)
                .message("Success")
                .build());

        return messages;
    }

    public ArrayList<RegisterResponse> validateEnoughCredit(HttpServletRequest request, @RequestBody RegisterSubjectDto form) {
        ArrayList<RegisterResponse> messages = new ArrayList<>();

        messages.add(RegisterResponse.builder()
                .success(true)
                .status(200L)
                .message("Success")
                .build());

        return messages;
    }

    public boolean isConflictTime(Long startTime1,Long endTime1,Long startTime2,Long endTime2){
        return  !(endTime1 <= startTime2 || startTime1 >= endTime2);
    }

}