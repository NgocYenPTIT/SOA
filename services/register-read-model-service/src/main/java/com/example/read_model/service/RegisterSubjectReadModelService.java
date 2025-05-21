package com.example.read_model.service;

import com.example.read_model.model.*;
import com.example.read_model.repository.RegisterSubjectRepository;
import com.example.read_model.util.ServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
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

//    public RegisterSubjectView getDetail(Long studentId) {
//        return this.registerSubjectRepository.findById(studentId).get();
//    }

    public List<OpeningSubject> getOpenSubjects(String token) throws  Exception{
            List<OpeningSubject> openingSubjects = new ArrayList<>();
//         Lay ra cac enrollment
            List<Enrollment> enrollments = (List<Enrollment>) this.serviceAPI.callForList(
                    this.enrollmentServiceUrl + "/enrollment",
                    HttpMethod.GET,
                    null,
                    Enrollment.class,
                    token
            );

            for(int i = 0 ; i < enrollments.size(); i++) {
                OpeningSubject openingSubject = new OpeningSubject();

                LinkedHashMap<String, Integer> hashmap = new LinkedHashMap<>((Map<String, Integer>) enrollments.get(i));
//             lay ra cac course ung voi cac enrollment do
                Course course = (Course) this.serviceAPI.call(
                        this.courseServiceUrl + "/course/" + hashmap.get("courseId"),
                        HttpMethod.GET,
                        null,
                        Course.class,
                        token
                );
                openingSubject.setCourseId(course.getId());
                openingSubject.setCourseCode(course.getCode());
                openingSubject.setCourseGroup(course.getCourseGroup());
                openingSubject.setPractiseGroup(course.getPractiseGroup());

                // lay ra cac subject ung voi cac course do
                Subject subject = (Subject) this.serviceAPI.call(
                        this.subjectServiceUrl + "/subject/" + course.getSubjectId(),
                        HttpMethod.GET,
                        null,
                        Subject.class,
                        token
                );
                openingSubject.setSubjectCode(subject.getSubjectCode());
                openingSubject.setSubjectName(subject.getSubjectName());
                openingSubject.setAmountOfCredit(subject.getCredit());
                openingSubject.setRemainSlot(course.getRemainSlot());
                openingSubject.setMaxStudent(course.getMaxStudents());

                // lay ra schedule ung voi cac course do
                List<ScheduleResponse> scheduleResponses = new ArrayList<>();
                List<Schedule> schedules = (List<Schedule>) this.serviceAPI.callForList(
                        this.scheduleServiceUrl + "/schedule?courseId=" + course.getId(),
                        HttpMethod.GET,
                        null,
                        Schedule.class,
                        token
                );

                // lay ra room + giao vien ung voi schedule do
                // lay ra cac subject ung voi cac course do
                for (int j = 0; j < schedules.size(); j++) {
                    LinkedHashMap<String, Object> hashmapp = new LinkedHashMap<>((Map) schedules.get(j));
                    Room room = (Room) this.serviceAPI.call(
                            this.roomServiceUrl + "/rooms/" + hashmapp.get("roomId"),
                            HttpMethod.GET,
                            null,
                            Room.class,
                            token
                    );
                    User teacher = (User) this.serviceAPI.call(
                            this.userServiceUrl + "/user/" + hashmapp.get("teacherId"),
                            HttpMethod.GET,
                            null,
                            User.class,
                            token
                    );
                    SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    Date startTime = iso8601Format.parse(hashmapp.get("startTime").toString());
                    Date endTime = iso8601Format.parse(hashmapp.get("endTime").toString());
                    int thu = startTime.getDay();
                    String day = startTime.getDate() + "/" + (startTime.getMonth() + 1 ) + "/" + (1900 + startTime.getYear());
                    String dayOfWeek = "";
                    switch (thu) {
                        case 1:
                            dayOfWeek = "Thứ 2";
                            break;
                        case 2:
                            dayOfWeek = "Thứ 3";
                            break;
                        case 3:
                            dayOfWeek = "Thứ 4";
                            break;
                        case 4:
                            dayOfWeek = "Thứ 5";
                            break;
                        case 5:
                            dayOfWeek = "Thứ 6";
                            break;
                        case 6:
                            dayOfWeek = "Thứ 7";
                            break;
                        case 0:
                            dayOfWeek = "Chủ nhật";
                    }
                    String content = dayOfWeek + ", " + startTime.getHours() + ":" + startTime.getMinutes() + " - " + endTime.getHours() + ":" + endTime.getMinutes() + " Ngày " + day +  " - " + room.getRoomCode() + " - " + teacher.getFullName();
                    scheduleResponses.add(ScheduleResponse.builder()
                            .id((((Integer)hashmapp.get("id")).longValue()))
                            .content(content)
                            .startTime(startTime.getTime())
                            .endTime(endTime.getTime())
                            .build());
                }
                openingSubject.setScheduleResponses(scheduleResponses);
                openingSubjects.add(openingSubject);
            }
            System.out.println(openingSubjects);
            return  openingSubjects;
    }

    public List<RegisteredSubject> getRegisteredSubjects(String token) throws  Exception {
            List<RegisteredSubject> registeredSubjects = new ArrayList<>();
//         Lay ra cac enrollment
            List<Enrollment> enrollments = (List<Enrollment>) this.serviceAPI.callForList(
                    this.enrollmentServiceUrl + "/enrollment",
                    HttpMethod.GET,
                    null,
                    Enrollment.class,
                    token
            );

            for(int i = 0 ; i < enrollments.size(); i++) {
                RegisteredSubject registeredSubject = new RegisteredSubject();

                LinkedHashMap<String, Integer> hashmap = new LinkedHashMap<>((Map<String, Integer>) enrollments.get(i));
//             lay ra cac course ung voi cac enrollment do
                Course course = (Course) this.serviceAPI.call(
                        this.courseServiceUrl + "/course/" + hashmap.get("courseId"),
                        HttpMethod.GET,
                        null,
                        Course.class,
                        token
                );
                registeredSubject.setCourseId(course.getId());
                registeredSubject.setCourseCode(course.getCode());
                registeredSubject.setCourseGroup(course.getCourseGroup());
                registeredSubject.setPractiseGroup(course.getPractiseGroup());

                Date updatedAt = course.getUpdatedAt();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String formattedDate = dateFormat.format(updatedAt);

                registeredSubject.setEnrollmentDate(formattedDate);
                registeredSubject.setStatus("REGISTERED");
                // lay ra cac subject ung voi cac course do
                Subject subject = (Subject) this.serviceAPI.call(
                        this.subjectServiceUrl + "/subject/" + course.getSubjectId(),
                        HttpMethod.GET,
                        null,
                        Subject.class,
                        token
                );
                registeredSubject.setSubjectCode(subject.getSubjectCode());
                registeredSubject.setSubjectName(subject.getSubjectName());
                registeredSubject.setAmountOfCredit(subject.getCredit());


                // lay ra schedule ung voi cac course do
                List<ScheduleResponse> scheduleResponses = new ArrayList<>();
                List<Schedule> schedules = (List<Schedule>) this.serviceAPI.callForList(
                        this.scheduleServiceUrl + "/schedule?courseId=" + course.getId(),
                        HttpMethod.GET,
                        null,
                        Schedule.class,
                        token
                );

                // lay ra room + giao vien ung voi schedule do
                // lay ra cac subject ung voi cac course do
                for (int j = 0; j < schedules.size(); j++) {
                    LinkedHashMap<String, Object> hashmapp = new LinkedHashMap<>((Map) schedules.get(j));
                    Room room = (Room) this.serviceAPI.call(
                            this.roomServiceUrl + "/rooms/" + hashmapp.get("roomId"),
                            HttpMethod.GET,
                            null,
                            Room.class,
                            token
                    );
                    User teacher = (User) this.serviceAPI.call(
                            this.userServiceUrl + "/user/" + hashmapp.get("teacherId"),
                            HttpMethod.GET,
                            null,
                            User.class,
                            token
                    );
                    SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    Date startTime = iso8601Format.parse(hashmapp.get("startTime").toString());
                    Date endTime = iso8601Format.parse(hashmapp.get("endTime").toString());
                    int thu = startTime.getDay();
                    String day = startTime.getDate() + "/" + (startTime.getMonth() + 1 ) + "/" + (1900 + startTime.getYear());
                    String dayOfWeek = "";
                    switch (thu) {
                        case 1:
                            dayOfWeek = "Thứ 2";
                            break;
                        case 2:
                            dayOfWeek = "Thứ 3";
                            break;
                        case 3:
                            dayOfWeek = "Thứ 4";
                            break;
                        case 4:
                            dayOfWeek = "Thứ 5";
                            break;
                        case 5:
                            dayOfWeek = "Thứ 6";
                            break;
                        case 6:
                            dayOfWeek = "Thứ 7";
                            break;
                        case 0:
                            dayOfWeek = "Chủ nhật";
                    }
                    String content = dayOfWeek + ", " + startTime.getHours() + ":" + startTime.getMinutes() + " - " + endTime.getHours() + ":" + endTime.getMinutes() + " Ngày " + day +  " - " + room.getRoomCode() + " - " + teacher.getFullName();
                    scheduleResponses.add(ScheduleResponse.builder()
                            .id((((Integer)hashmapp.get("id")).longValue()))
                            .content(content)
                            .startTime(startTime.getTime())
                            .endTime(endTime.getTime())
                            .build());
                }
                registeredSubject.setScheduleResponses(scheduleResponses);
                registeredSubjects.add(registeredSubject);
            }
            System.out.println(registeredSubjects);
            return registeredSubjects;
    }

    public  void update(UpdateReadModelEvent event) throws Exception{
        System.out.println("UPDATING READ MODEL......");

        List<OpeningSubject> openingSubjects = getOpenSubjects(event.getToken());
            List<RegisteredSubject> registeredSubjects = getRegisteredSubjects(event.getToken());

            RegisterSubjectView view = RegisterSubjectView.builder()
                    .id(event.getStudentId())
                    .semester("1")
                    .year("2024-2025")
                    .endOfEnrollmentTime("00:00:00 01-01-2026")
                    .numOfRegisteredSubject(registeredSubjects.size())
                    .numOfRegisteredCredit(registeredSubjects.stream().mapToInt(RegisteredSubject::getAmountOfCredit).sum())
                    .openSubject(openingSubjects)
                    .registeredSubject(registeredSubjects)
                    .build();
            this.registerSubjectRepository.save(view);
    }

}