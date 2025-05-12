package com.example.register_subject_service.controller;

import com.example.register_subject_service.model.CourseRegistrationEvent;
import com.example.register_subject_service.model.CourseRegistrationRequest;
import com.example.register_subject_service.service.EventStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/course-registration")
public class CourseRegistrationController {

    private final EventStoreService eventStoreService;

    @Autowired
    public CourseRegistrationController(EventStoreService eventStoreService) {
        this.eventStoreService = eventStoreService;
    }

    @PostMapping
    public ResponseEntity<String> registerCourse(@RequestBody CourseRegistrationRequest request) {
        try {
            CourseRegistrationEvent event = new CourseRegistrationEvent(
                    request.getStudentId(),
                    request.getCourseId(),
                    "REGISTER"
            );

            eventStoreService.saveEvent(event);

            return ResponseEntity.ok("Course registration event saved successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
