package com.example.read_model.service;

import com.example.read_model.model.*;
import com.example.read_model.repository.RegisterSubjectRepository;
import com.example.read_model.util.ServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class RegisterSubjectReadModelService {

    private final RegisterSubjectRepository registerSubjectRepository;
    private  final ServiceAPI serviceAPI;

    @Value("${app.global.enrollment-service-url}")
    private String enrollmentServiceUrl;

    @Value("${app.global.user-service-url}")
    private String userServiceUrl;

    @Value("${app.global.course-service-url}")
    private String courseServiceUrl;

    @Value("${app.global.room-service-url}")
    private String roomServiceUrl;

    @Value("${app.global.subject-service-url}")
    private String subjectServiceUrl;

    @Value("${app.global.schedule-service-url}")
    private String scheduleServiceUrl;



    @Autowired
    public RegisterSubjectReadModelService(RegisterSubjectRepository registerSubjectRepository, ServiceAPI serviceAPI) {
        this.registerSubjectRepository = registerSubjectRepository;
        this.serviceAPI = serviceAPI;
    }

    public boolean getRegisteredSubjects(Long id){
        Long studentId =1L;
//         Lay ra cac enrollment
        List<Enrollment> enrollments = (List<Enrollment>) this.serviceAPI.callForList(
                this.enrollmentServiceUrl + "/enrollment",
                HttpMethod.GET,
                null,
                Enrollment.class,
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmMiLCJpZCI6MSwiaWF0IjoxNzQ3Njc0ODIzLCJleHAiOjE3NDc3MTA4MjN9.osyLJodtWVbDhxV1QKlsGNjdzmgDDd758QLvfjy6vtg"
        );

        for(int i = 0 ; i < enrollments.size(); i++) {
            LinkedHashMap<String, Integer> hashmap = new LinkedHashMap<>((Map<String, Integer>) enrollments.get(i));
//             lay ra cac course ung voi cac enrollment do
            Course course = (Course) this.serviceAPI.call(
                    this.courseServiceUrl + "/course/" + hashmap.get("courseId"),
                    HttpMethod.GET,
                    null,
                    Course.class,
                    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmMiLCJpZCI6MSwiaWF0IjoxNzQ3Njc0ODIzLCJleHAiOjE3NDc3MTA4MjN9.osyLJodtWVbDhxV1QKlsGNjdzmgDDd758QLvfjy6vtg"
            );
            // lay ra cac subject ung voi cac course do
            Subject subject = (Subject) this.serviceAPI.call(
                    this.subjectServiceUrl + "/subject/" + course.getSubjectId(),
                    HttpMethod.GET,
                    null,
                    Subject.class,
                    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmMiLCJpZCI6MSwiaWF0IjoxNzQ3Njc0ODIzLCJleHAiOjE3NDc3MTA4MjN9.osyLJodtWVbDhxV1QKlsGNjdzmgDDd758QLvfjy6vtg"
            );
            // lay ra schedule ung voi cac course do
            List<Schedule> schedules = (List<Schedule>) this.serviceAPI.callForList(
                    this.scheduleServiceUrl + "/schedule?courseId=" + course.getId(),
                    HttpMethod.GET,
                    null,
                    Schedule.class,
                    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmMiLCJpZCI6MSwiaWF0IjoxNzQ3Njc0ODIzLCJleHAiOjE3NDc3MTA4MjN9.osyLJodtWVbDhxV1QKlsGNjdzmgDDd758QLvfjy6vtg"
            );

            // lay ra room + giao vien ung voi schedule do
            // lay ra cac subject ung voi cac course do
            for (int j = 0; j < schedules.size(); j++) {
                LinkedHashMap<String, Integer> hashmapp = new LinkedHashMap<>((Map) schedules.get(j));
                Room room = (Room) this.serviceAPI.call(
                        this.roomServiceUrl + "/rooms/" + hashmapp.get("roomId"),
                        HttpMethod.GET,
                        null,
                        Room.class,
                        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmMiLCJpZCI6MSwiaWF0IjoxNzQ3Njc0ODIzLCJleHAiOjE3NDc3MTA4MjN9.osyLJodtWVbDhxV1QKlsGNjdzmgDDd758QLvfjy6vtg"
                );
                User teacher = (User) this.serviceAPI.call(
                        this.userServiceUrl + "/user/" + hashmapp.get("teacherId"),
                        HttpMethod.GET,
                        null,
                        User.class,
                        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmMiLCJpZCI6MSwiaWF0IjoxNzQ3Njc0ODIzLCJleHAiOjE3NDc3MTA4MjN9.osyLJodtWVbDhxV1QKlsGNjdzmgDDd758QLvfjy6vtg"
                );
            }
        }

        return true;
    }

    public void update(UpdateReadModelEvent event){
        System.out.println("UPDATING READ MODEL......");
        Long studentId = event.getStudentId();
        // Lay ra cac enrollment
        List<Enrollment> enrollments = (List<Enrollment>) this.serviceAPI.callForList(
                this.enrollmentServiceUrl + "/enrollment",
                HttpMethod.GET,
                null,
                Enrollment.class,
                event.getToken()
        );
        System.out.println(enrollments);
        // lay ra cac course ung voi enrollment do
        // lay ra cac subject ung voi cac course do
        // lay ra schedule ung voi cac course do
        // lay ra room + giao vien ung voi schedule do
    }
}