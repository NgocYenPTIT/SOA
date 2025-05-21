package com.example.course_service.service;

import com.example.course_service.model.*;
import com.example.course_service.repository.CourseRepository;
import com.example.course_service.repository.OutboxRepository;
import com.example.course_service.repository.TransactionLogRepository;
import com.example.course_service.util.ServiceAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private OutboxRepository outboxRepository;

    private final TransactionLogRepository transactionLogRepository;

    private final ServiceAPI serviceAPI ;

    @Value("${app.global.wish-subject-service-url}")
    private String wishSubjectURL;


    @Autowired
    public CourseService(CourseRepository courseRepository, TransactionLogRepository transactionLogRepository, OutboxRepository outboxRepository, ServiceAPI serviceAPI) {
        this.serviceAPI = serviceAPI;
        this.outboxRepository = outboxRepository;
        this.transactionLogRepository = transactionLogRepository;
        this.courseRepository = courseRepository;
    }

    public List<Course> getListCourse(Long semesterId) {
        return this.courseRepository.findAllBySemesterIdAndDeletedAtIsNull(semesterId);
    }

    public Course getDetail(Long id) {
        return this.courseRepository.findById(id).get();
    }
    @Transactional
    public void changeQuantitySlot(ChangeQuantitySlotEvent event) throws Exception {

        if(this.validateDuplicateEvent(event)){
            System.out.println("CHANGE QUANTITY SLOT EVENT DUPLICATE");
            return;
        }

        try {
            List<List<Long>> addAndDeleteCourses = event.getAddAndDeleteCourses();

            List<Long> addCourses = addAndDeleteCourses.get(0);
            List<Long> deleteCourses = addAndDeleteCourses.get(1);

            this.increment(addCourses);
            this.decrement(deleteCourses);

            //emit COMMIT
            System.out.println("CHANGE SUCCESS -> COMMIT");

            String eventType = "CommitChangeQuantitySlotEvent";

            CommitEvent commitEvent = CommitEvent.builder()
                    .eventId(java.util.UUID.randomUUID())
                    .eventType(eventType)
                    .correlationId(event.getCorrelationId())
                    .studentId(event.getStudentId())
                    .addAndDeleteCourses(addAndDeleteCourses)
                    .token(event.getToken())
                    .timestamp(System.currentTimeMillis())
                    .build();

            System.out.println("commitEvent: " + commitEvent);
            // Save outbox
            this.outboxRepository.save(OutBoxMessage.builder().eventType(eventType).payload(new ObjectMapper().writeValueAsString(commitEvent)).build());
            this.transactionLogRepository.save(TransactionLog.builder().correlationId(event.getCorrelationId()).status("COMMIT").build());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    private boolean validateDuplicateEvent(ChangeQuantitySlotEvent event){
        try {
            this.transactionLogRepository.save(TransactionLog.builder().correlationId(event.getCorrelationId()).status("START_TRANSACTION").build());
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return  true;
        }
    }

    private void increment(List<Long> addCourses) {
        for(int i = 0 ; i< addCourses.size(); i++){
            Long courseId = addCourses.get(i);
            this.courseRepository.incrementCurrentStudents(courseId);
        }
    }

    private void decrement(List<Long> deleteCourses) {
        for(int i = 0 ; i< deleteCourses.size(); i++){
            Long courseId = deleteCourses.get(i);
            this.courseRepository.decrementCurrentStudents(courseId);
        }
    }

    public List<Course> getListOpenCourse(Long studentId , String token) {
        List<WishSubject> wishSubjects = (List<WishSubject>) this.serviceAPI.callForList(
                this.wishSubjectURL + "/wish-subject",
                HttpMethod.GET,
                null,
                WishSubject.class,
               token
        );
        List<Long> subjectIds = new ArrayList<>();
        for(int i = 0 ; i < wishSubjects.size(); i++){
            LinkedHashMap<String, Object> hashmap = new LinkedHashMap<>((Map) wishSubjects.get(i));
            subjectIds.add(((Number) hashmap.get("subjectId")).longValue()) ;

        }

        List<Course> courses = this.courseRepository.findAllBySubjectIdInAndDeletedAtIsNull(subjectIds);
        return  courses;
    }

}