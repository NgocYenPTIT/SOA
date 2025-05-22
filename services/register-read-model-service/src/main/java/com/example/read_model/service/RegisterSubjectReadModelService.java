package com.example.read_model.service;

import com.example.read_model.model.*;
import com.example.read_model.repository.RegisterSubjectRepository;
import com.example.read_model.util.ServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Value("${app.global.credit-rule-service-url}")
    private String creditRuleServiceUrl;

    @Value("${app.global.semester}")
    private String  semester;

    @Value("${app.global.year}")
    private String year;

    @Value("${app.global.endOfEnrollmentTime}")
    private String  endOfEnrollmentTime;


    @Autowired
    public RegisterSubjectReadModelService(RegisterSubjectRepository registerSubjectRepository, ServiceAPI serviceAPI) {
        this.registerSubjectRepository = registerSubjectRepository;
        this.serviceAPI = serviceAPI;
    }

    public RegisterSubjectView getView(Long studentId,String token) {
        RegisterSubjectView view = this.registerSubjectRepository.findById(studentId).orElse(null);
        if(view == null) {
            return this.seeding(studentId, token);
        }
        return view;
    }

    public List<OpeningSubject> getOpenSubjects(String token) throws  Exception{
            List<OpeningSubject> openingSubjects = new ArrayList<>();
//         Lay ra cac enrollment
            List<Course> courses = (List<Course>) this.serviceAPI.callForList(
                    this.courseServiceUrl + "/course-open",
                    HttpMethod.GET,
                    null,
                    Course.class,
                    token
            );
        System.out.println("xinchao");
        System.out.println(courses);

            for(int i = 0 ; i < courses.size(); i++) {
                OpeningSubject openingSubject = new OpeningSubject();

                LinkedHashMap<String, Object> hashmap = new LinkedHashMap<>((Map<String, Integer>) courses.get(i));
//             lay ra cac course ung voi cac enrollment do
                openingSubject.setCourseId(((Number)hashmap.get("id")).longValue());
                openingSubject.setCourseCode((String)hashmap.get("code"));
                openingSubject.setCourseGroup((String)hashmap.get("courseGroup"));
                openingSubject.setPractiseGroup((String)hashmap.get("practiseGroup"));

                // lay ra cac subject ung voi cac course do
                Subject subject = (Subject) this.serviceAPI.call(
                        this.subjectServiceUrl + "/subject/" + hashmap.get("subjectId"),
                        HttpMethod.GET,
                        null,
                        Subject.class,
                        token
                );
                openingSubject.setSubjectCode(subject.getSubjectCode());
                openingSubject.setSubjectName(subject.getSubjectName());
                openingSubject.setAmountOfCredit(subject.getCredit());
                openingSubject.setRemainSlot((Integer) hashmap.get("remainSlot"));
                openingSubject.setMaxStudent((Integer) hashmap.get("maxStudents"));

                // lay ra schedule ung voi cac course do
                List<ScheduleResponse> scheduleResponses = new ArrayList<>();
                List<Schedule> schedules = (List<Schedule>) this.serviceAPI.callForList(
                        this.scheduleServiceUrl + "/schedule?courseId=" + hashmap.get("id"),
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
        System.out.println("UPDATING READ MODEL WITH STATUS..........   " + event.getStatus() +"  ......");
        Long studentId = event.getStudentId();
        RegisterSubjectView viewSaved = this.registerSubjectRepository.findById(studentId).orElse(null);

        if(viewSaved == null) {
            return;
        }
        else if( event.getTimestamp() < viewSaved.getLastUpdate()) {
            return;
        }
        //4 loai message : FAIL VALIDATE, COMMIT, ROLLBACK, FULL

        if(event.isSuccess() && event.getStatus().equals("COMMIT")){
            List<OpeningSubject> openingSubjects = getOpenSubjects(event.getToken());
            List<RegisteredSubject> registeredSubjects = getRegisteredSubjects(event.getToken());
            User student = (User)this.serviceAPI.call(
                    this.userServiceUrl + "/user/" + event.getStudentId(),
                    HttpMethod.GET,
                    null,
                    User.class,
                    event.getToken()
            );

            Long semesterId = student.getSemesterId();

            CreditRule creditRule = this.serviceAPI.call(
                    this.creditRuleServiceUrl + "/credit-rule/semester/" + semesterId,
                    HttpMethod.GET,
                    null,
                    CreditRule.class,
                    event.getToken()
            );


            RegisterSubjectView view = RegisterSubjectView.builder()
                    .id(event.getStudentId())
                    .semester(semester)
                    .year(year)
                    .minimumCreditSemester(creditRule.getMinCredits())
                    .endOfEnrollmentTime(endOfEnrollmentTime)
                    .numOfRegisteredSubject(registeredSubjects.size())
                    .numOfRegisteredCredit(registeredSubjects.stream().mapToInt(RegisteredSubject::getAmountOfCredit).sum())
                    .openSubject(openingSubjects)
                    .registeredSubject(registeredSubjects)
                    .status("AVAILABLE AFTER COMMIT")
                    .messages(event.getMessages())
                    .lastUpdate(event.getTimestamp())
                    .build();
            this.registerSubjectRepository.save(view);
        }
        else if(event.getStatus().equals("FAIL VALIDATE")){
            viewSaved.setStatus("AVAILABLE AFTER FAIL VALIDATE");
            viewSaved.setMessages(event.getMessages());
            this.registerSubjectRepository.save(viewSaved);
        }
        else if(event.getStatus().equals("ROLLBACK")){
            viewSaved.setStatus("AVAILABLE AFTER ROLLBACK");
            viewSaved.setMessages(event.getMessages());
            this.registerSubjectRepository.save(viewSaved);
        }
        else if(event.getStatus().equals("PROCESSING")){
            viewSaved.setStatus("PROCESSING");
            this.registerSubjectRepository.save(viewSaved);
        }
        else if(event.getStatus().contains("FULL")){
            viewSaved.setStatus("AVAILABLE AFTER FULL");
            viewSaved.setMessages(event.getMessages());
            this.registerSubjectRepository.save(viewSaved);
        }
    }

    public RegisterSubjectView seeding(Long studentId, String token){
        System.out.println("SEEDING.......... ");

       try {
           List<OpeningSubject> openingSubjects = getOpenSubjects(token);
           List<RegisteredSubject> registeredSubjects = getRegisteredSubjects(token);
           User student = (User)this.serviceAPI.call(
                   this.userServiceUrl + "/user/" + studentId,
                   HttpMethod.GET,
                   null,
                   User.class,
                   token
           );

           Long semesterId = student.getSemesterId();

           CreditRule creditRule = this.serviceAPI.call(
                   this.creditRuleServiceUrl + "/credit-rule/semester/" + semesterId,
                   HttpMethod.GET,
                   null,
                   CreditRule.class,
                   token
           );
           RegisterSubjectView view = RegisterSubjectView.builder()
                   .id(studentId)
                   .semester(semester)
                   .year(year)
                   .minimumCreditSemester(creditRule.getMinCredits())
                   .endOfEnrollmentTime(endOfEnrollmentTime)
                   .numOfRegisteredSubject(registeredSubjects.size())
                   .numOfRegisteredCredit(registeredSubjects.stream().mapToInt(RegisteredSubject::getAmountOfCredit).sum())
                   .openSubject(openingSubjects)
                   .registeredSubject(registeredSubjects)
                   .status("AVAILABLE FROM SEED")
                   .messages(new ArrayList<>(Collections.singleton("")))
                   .lastUpdate(new Date().getTime())
                   .build();
           return  this.registerSubjectRepository.save(view);
       }
       catch (Exception e) {e.printStackTrace();return new RegisterSubjectView();}
    }
}