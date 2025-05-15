package com.example.register_subject_service.service;

import com.example.register_subject_service.model.*;
import com.example.register_subject_service.service.event.EventStoreService;
import com.example.register_subject_service.util.ServiceAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    private final EventStoreService eventStoreService;

    static ArrayList<Slot> slots;
    static Long lock;

    public RegisterSubjectService(ServiceAPI serviceAPI, EventStoreService eventStoreService) {
        this.eventStoreService = eventStoreService;
        this.serviceAPI = serviceAPI;
    }


    public List<RegisterResponse> registerSubject(HttpServletRequest request, @RequestBody RegisterSubjectRequest form) {

        try {
            CourseRegistrationEvent event = CourseRegistrationEvent.builder()
                    .eventId(java.util.UUID.randomUUID())
                    .correlationId(java.util.UUID.randomUUID().toString())
                    .studentId((Long) request.getAttribute("id"))
                    .courseIds(form.getCourseIds())
                    .token((String)request.getAttribute("token"))
                    .build();

            // emit event and send data attach to processing
            this.eventStoreService.saveEvent(event);
            //success
            return Collections.singletonList(RegisterResponse.builder().success(true).status(202L).message("Processing...").build());


        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}