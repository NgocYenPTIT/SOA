package com.example.course_service.service;

import com.example.course_service.model.*;
import com.example.course_service.repository.CourseRepository;
import com.example.course_service.repository.OutboxRepository;
import com.example.course_service.repository.TransactionLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private OutboxRepository outboxRepository;

    private final TransactionLogRepository transactionLogRepository;
    @Autowired
    public CourseService(CourseRepository courseRepository, TransactionLogRepository transactionLogRepository, OutboxRepository outboxRepository) {
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

            //emit SUCCESS
            System.out.println("CHANGE SUCCESS");

            String eventType = "RegisterStatus";

            RegisterStatusEvent registerStatusEvent = RegisterStatusEvent.builder()
                    .eventId(java.util.UUID.randomUUID())
                    .eventType(eventType)
                    .correlationId(event.getCorrelationId())
                    .studentId(event.getStudentId())
                    .messages(new ArrayList<>(Collections.singleton("SUCCESS")))
                    .token(event.getToken())
                    .timestamp(System.currentTimeMillis())
                    .build();

            System.out.println("RegisterStatusEvent: " + registerStatusEvent);
            // Save outbox
            this.outboxRepository.save(OutBoxMessage.builder().eventType(eventType).payload(new ObjectMapper().writeValueAsString(registerStatusEvent)).build());

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
}