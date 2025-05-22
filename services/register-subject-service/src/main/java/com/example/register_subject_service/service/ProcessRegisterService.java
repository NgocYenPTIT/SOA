package com.example.register_subject_service.service;

import com.example.register_subject_service.model.*;
import com.example.register_subject_service.repository.OutBoxMessageRepository;
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

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProcessRegisterService {

    @Value("${app.global.schedule-service-url}")
    private String scheduleURL;

    @Value("${app.global.credit-rule-service-url}")
    private String creditRuleURL;

    @Value("${app.global.subject-service-url}")
    private String subjectURL;

    @Value("${app.global.wish-subject-service-url}")
    private String wishSubjectURL;


    @Value("${app.global.user-service-url}")
    private String userURL;

    @Value("${app.global.course-service-url}")
    private String courseURL;

    @Value("${app.global.enrollment-service-url}")
    private String enrollmentURL;

    @Autowired
    private  ServiceAPI serviceAPI;

    private final OutBoxMessageRepository outBoxMessageRepository;


    public ProcessRegisterService(ServiceAPI serviceAPI, OutBoxMessageRepository outBoxMessageRepository) {
        this.outBoxMessageRepository = outBoxMessageRepository;
        this.serviceAPI = serviceAPI;

    }

    @Transactional
    public void call(CourseRegistrationEvent event)throws Exception {
        System.out.println("Processing registration event: " + event);

        List<RegisterResponse> messages = new ArrayList<>();

        //validate
        messages.addAll(this.validate(event));
        if (!messages.isEmpty()) {
            System.out.println(messages);
            this.emitFailValidate(event,messages);
            return;
        }
        // save
        this.save(event);
    }

    private void emitFailValidate(CourseRegistrationEvent event, List<RegisterResponse> messages) throws Exception {
        String eventType = "UpdateReadModelEvent";

        UpdateReadModelEvent updateReadModelEvent = UpdateReadModelEvent.builder()
                .eventId(java.util.UUID.randomUUID())
                .eventType(eventType)
                .correlationId(event.getCorrelationId())
                .studentId(event.getStudentId())
                .success(false)
                .status("FAIL VALIDATE")
                .messages(messages.stream()
                        .map(RegisterResponse::getMessage)
                        .collect(Collectors.toList()))
                .token(event.getToken())
                .timestamp(System.currentTimeMillis())
                .build();
        System.out.println("updateReadModelEvent: " + updateReadModelEvent);
        // Save outbox
        this.outBoxMessageRepository.save(OutBoxMessage.builder().eventType(eventType).payload(new ObjectMapper().writeValueAsString(updateReadModelEvent)).build());
        System.out.println("FAIL VALIDATE");
    }

    public void save(CourseRegistrationEvent event) throws Exception {
       List<List<Long>> addAndDeleteCourse = this.findAddAndDeleteCourse(event);
        //serve slot
        String eventType = "ReserveSlotEvent";

        ReserveSlotEvent serveSlotEvent = ReserveSlotEvent.builder()
                .eventId(java.util.UUID.randomUUID())
                .eventType(eventType)
                .correlationId(java.util.UUID.randomUUID().toString())
                .studentId(event.getStudentId())
                .addAndDeleteCourses(addAndDeleteCourse)
                .token(event.getToken())
                .timestamp(System.currentTimeMillis())
                .build();

        // Save outbox
        this.outBoxMessageRepository.save(OutBoxMessage.builder().eventType(eventType).payload(new ObjectMapper().writeValueAsString(serveSlotEvent)).build());
    }

    public List<RegisterResponse> validate(CourseRegistrationEvent event) {
        List<RegisterResponse> messages = new ArrayList<>();

        boolean canRegister = this.canRegister(event);
        if (!canRegister) {
            messages.add(RegisterResponse.builder()
                    .success(false)
                    .status(400L)
                    .message("You are not allowed to register this course")
                    .build());
            return messages;
        }

        messages.addAll(this.validateConflictSchedule(event));
        messages.addAll(this.validateEnoughCredit(event));

        return messages;
    }

    public List<List<Long> > findAddAndDeleteCourse(CourseRegistrationEvent event) {
        List<Enrollment> enrollments = (List<Enrollment>) this.serviceAPI.callForList(
                this.enrollmentURL + "/enrollment",
                HttpMethod.GET,
                null,
                Enrollment.class,
                event.getToken()
        );

        HashSet<Long> courseIdsRegistered = new HashSet<>();
        List<Long> courseIdsAdd = new ArrayList<>();
        List<Long> courseIdsDelete = new ArrayList<>();


        for(int i = 0 ; i < enrollments.size(); i++){
            LinkedHashMap<String, Integer> hashmap = new LinkedHashMap<>((Map) enrollments.get(i));
            courseIdsRegistered.add((long) hashmap.get("courseId"));
        }
        System.out.println("courseIdsRegistered: " + courseIdsRegistered);

        event.getCourseIds().forEach(courseId -> {
            if(!courseIdsRegistered.contains(courseId)){
                courseIdsAdd.add(courseId);
            }
        });
        courseIdsRegistered.forEach(courseId -> {
            if(!event.getCourseIds().contains(courseId)){
                courseIdsDelete.add(courseId);
            }
        });

        System.out.println("courseIdsAdd: " + courseIdsAdd);
        System.out.println("courseIdsDelete: " + courseIdsDelete);
        return new ArrayList<>(Arrays.asList(courseIdsAdd,courseIdsDelete));
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
        return messages;
    }

    public ArrayList<RegisterResponse> validateEnoughCredit(CourseRegistrationEvent event) {
        ArrayList<RegisterResponse> messages = new ArrayList<>();

        User student = (User)this.serviceAPI.call(
                this.userURL + "/user/" + event.getStudentId(),
                HttpMethod.GET,
                null,
                User.class,
                event.getToken()
        );

        Long semesterId = student.getSemesterId();

        CreditRule creditRule = this.serviceAPI.call(
                this.creditRuleURL + "/credit-rule/semester/" + semesterId,
                HttpMethod.GET,
                null,
                CreditRule.class,
                event.getToken()
        );
        System.out.println(creditRule);
        Integer minCredits = creditRule.getMinCredits();
        Integer maxCredits = creditRule.getMaxCredits();
        Integer totalCredits = 0;

        for (int i = 0; i < event.getCourseIds().size(); i++) {
            Course course = (Course)this.serviceAPI.call(
                    this.courseURL + "/course/" + event.getCourseIds().get(i),
                    HttpMethod.GET,
                    null,
                    Course.class,
                    event.getToken()
            );
            Subject subject = (Subject)this.serviceAPI.call(
                    this.subjectURL + "/subject/" + course.getSubjectId(),
                    HttpMethod.GET,
                    null,
                    Subject.class,
                    event.getToken()
            );
            totalCredits += subject.getCredit();
        }

        System.out.println("minCredits: " + minCredits);
        System.out.println("maxCredits: " + maxCredits);
        System.out.println("totalCredits: " + totalCredits);

        if(!(minCredits <= totalCredits && totalCredits <= maxCredits)) {
            messages.add(RegisterResponse.builder()
                    .success(false)
                    .status(400L)
                    .message("Credit not enough in semester")
                    .build());
        }

        return messages;
    }

    public boolean isConflictTime(Long startTime1, Long endTime1, Long startTime2, Long endTime2) {
        System.out.println("is validating conflict time");
        return !(endTime1 <= startTime2 || startTime1 >= endTime2);
    }

    public boolean canRegister(CourseRegistrationEvent event){
        // Lay ra cac mon nguyen vong cua sinh vien
        // Kiem tra tong so tin chi
        // Neu tong < minCredits thi return false
        List<WishSubject> wishSubjects = (List<WishSubject>) this.serviceAPI.callForList(
                this.wishSubjectURL + "/wish-subject",
                HttpMethod.GET,
                null,
                WishSubject.class,
                event.getToken()
        );

        Integer sumCredits = 0;
        for(int i = 0 ; i < wishSubjects.size(); i++){
            LinkedHashMap<String, Integer> hashmap = new LinkedHashMap<>((Map) wishSubjects.get(i));
            Subject subject = this.serviceAPI.call(
                    this.subjectURL + "/subject/" + hashmap.get("subjectId"),
                    HttpMethod.GET,
                    null,
                    Subject.class,
                    event.getToken()
            );
            sumCredits += subject.getCredit();
        }
        System.out.println("sumCredits: " + sumCredits);

        User student = (User)this.serviceAPI.call(
                this.userURL + "/user/" + event.getStudentId(),
                HttpMethod.GET,
                null,
                User.class,
                event.getToken()
        );

        Long semesterId = student.getSemesterId();

        CreditRule creditRule = this.serviceAPI.call(
                this.creditRuleURL + "/credit-rule/semester/" + semesterId,
                HttpMethod.GET,
                null,
                CreditRule.class,
                event.getToken()
        );
        System.out.println(creditRule);

        Integer minCredits = creditRule.getMinCredits();

        if (sumCredits < minCredits) {
            return false;
        }
        return true;
    }
    public boolean validateFirst(Long studentId, String token){
        // Lay ra cac mon nguyen vong cua sinh vien
        // Kiem tra tong so tin chi
        // Neu tong < minCredits thi return false
        List<WishSubject> wishSubjects = (List<WishSubject>) this.serviceAPI.callForList(
                this.wishSubjectURL + "/wish-subject",
                HttpMethod.GET,
                null,
                WishSubject.class,
                token
        );

        Integer sumCredits = 0;
        for(int i = 0 ; i < wishSubjects.size(); i++){
            LinkedHashMap<String, Integer> hashmap = new LinkedHashMap<>((Map) wishSubjects.get(i));
            Subject subject = this.serviceAPI.call(
                    this.subjectURL + "/subject/" + hashmap.get("subjectId"),
                    HttpMethod.GET,
                    null,
                    Subject.class,
                    token
            );
            sumCredits += subject.getCredit();
        }
        System.out.println("sumCredits: " + sumCredits);

        User student = (User)this.serviceAPI.call(
                this.userURL + "/user/" + studentId,
                HttpMethod.GET,
                null,
                User.class,
                token
        );

        Long semesterId = student.getSemesterId();

        CreditRule creditRule = this.serviceAPI.call(
                this.creditRuleURL + "/credit-rule/semester/" + semesterId,
                HttpMethod.GET,
                null,
                CreditRule.class,
                token
        );
        System.out.println(creditRule);

        Integer minCredits = creditRule.getMinCredits();

        if (sumCredits < minCredits) {
            return false;
        }
        return true;
    }

}
