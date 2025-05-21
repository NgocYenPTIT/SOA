package com.example.enrollment_service.service;

import com.example.enrollment_service.model.*;
import com.example.enrollment_service.repository.EnrollmentRepository;
import com.example.enrollment_service.repository.OutBoxRepository;
import com.example.enrollment_service.repository.TransactionLogRepository;
import com.example.enrollment_service.util.ServiceAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class EnrollmentService {

    private  EnrollmentRepository enrollmentRepository;
    private TransactionLogRepository transactionLogRepository;
    private OutBoxRepository outBoxRepository;
    private ServiceAPI serviceAPI;
    @Value("${app.global.course-service-url}")
    private String courseServiceUrl;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, TransactionLogRepository transactionLogRepository, OutBoxRepository outBoxRepository, ServiceAPI serviceAPI) {
        this.serviceAPI = serviceAPI;
        this.enrollmentRepository = enrollmentRepository;
        this.transactionLogRepository = transactionLogRepository;
        this.outBoxRepository = outBoxRepository;
    }

    public List<Enrollment> getList(HttpServletRequest request){
        return this.enrollmentRepository.findByStudentIdAndDeletedAtIsNullAndStatus((Long)request.getAttribute("id"), "REGISTERED");
    }

    @Transactional
    public void reserve(ReserveSlotEvent event) throws Exception {
           if(this.validateDuplicateEvent(event)){
               System.out.println("RESERVE DUPLICATE");
               return;
           }

           try {
               Long studentId = event.getStudentId();

               List<List<Long>> addAndDeleteCourses = event.getAddAndDeleteCourses();

               List<Long> addCourses = addAndDeleteCourses.get(0);

               add(addCourses, studentId,event.getToken());

               //emit SUCCESS
               System.out.println("RESERVE SUCCESS");

               String eventType = "ChangeQuantitySlotEvent";

               ReduceSlotEvent reduceSlotEvent = ReduceSlotEvent.builder()
                       .eventId(java.util.UUID.randomUUID())
                       .eventType(eventType)
                       .correlationId(event.getCorrelationId())
                       .studentId(event.getStudentId())
                       .addAndDeleteCourses(addAndDeleteCourses)
                       .token(event.getToken())
                       .timestamp(System.currentTimeMillis())
                       .build();
               System.out.println("ReduceSlotEvent: " + reduceSlotEvent);
               // Save outbox
               this.outBoxRepository.save(OutBoxMessage.builder().eventType(eventType).payload(new ObjectMapper().writeValueAsString(reduceSlotEvent)).build());

           } catch (Exception e) {
               e.printStackTrace();
               //emit FULL
               String eventType = "UpdateReadModelEvent";

               UpdateReadModelEvent updateReadModelEvent = UpdateReadModelEvent.builder()
                       .eventId(java.util.UUID.randomUUID())
                       .eventType(eventType)
                       .correlationId(event.getCorrelationId())
                       .studentId(event.getStudentId())
                       .success(false)
                       .status(e.getMessage())
                       .messages(new ArrayList<>(Collections.singleton(e.getMessage())))
                       .token(event.getToken())
                       .timestamp(System.currentTimeMillis())
                       .build();
               System.out.println("updateReadModelEvent: " + updateReadModelEvent);
               // Save outbox
               this.outBoxRepository.save(OutBoxMessage.builder().eventType(eventType).payload(new ObjectMapper().writeValueAsString(updateReadModelEvent)).build());
               System.out.println("RESERVE FULL");
               throw e;
           }
    }
    @Transactional
    public void rollback(RollBackEvent event) throws Exception {
        if(this.validateDuplicateRollbackEvent(event)){
            System.out.println("ROLLBACK IS DUPLICATE");
            return;
        }

            Long studentId = event.getStudentId();

            List<List<Long>> addAndDeleteCourses = event.getAddAndDeleteCourses();

            List<Long> addCourses = addAndDeleteCourses.get(0);

            delete(addCourses, studentId);

            //emit ROLLBACK SUCCESS
            System.out.println("ROLLBACK SUCCESS");

            String eventType = "UpdateReadModelEvent";

            UpdateReadModelEvent updateReadModelEvent = UpdateReadModelEvent.builder()
                    .eventId(java.util.UUID.randomUUID())
                    .eventType(eventType)
                    .correlationId(event.getCorrelationId())
                    .studentId(event.getStudentId())
                    .success(false)
                    .status("ROLLBACK")
                    .messages(new ArrayList<>(Collections.singleton("ROLLBACK")))
                    .token(event.getToken())
                    .timestamp(System.currentTimeMillis())
                    .build();
            System.out.println("updateReadModelEvent: " + updateReadModelEvent);
            // Save outbox
            this.outBoxRepository.save(OutBoxMessage.builder().eventType(eventType).payload(new ObjectMapper().writeValueAsString(updateReadModelEvent)).build());
            System.out.println("ROLLBACK BECAUSE SYSTEM ERROR");

    }

    @Transactional
    public void commit(CommitEvent event) throws Exception {
        if(this.validateDuplicateCommitEvent(event)){
            System.out.println("COMMIT IS DUPLICATE");
            return;
        }

            Long studentId = event.getStudentId();

            List<List<Long>> addAndDeleteCourses = event.getAddAndDeleteCourses();

            List<Long> deleteCourses = addAndDeleteCourses.get(1);

            this.delete(deleteCourses, studentId);

            //emit COMMIT SUCCESS
            System.out.println("COMMIT SUCCESS");

            String eventType = "UpdateReadModelEvent";

            UpdateReadModelEvent updateReadModelEvent = UpdateReadModelEvent.builder()
                    .eventId(java.util.UUID.randomUUID())
                    .eventType(eventType)
                    .correlationId(event.getCorrelationId())
                    .studentId(event.getStudentId())
                    .success(true)
                    .status("COMMIT")
                    .messages(new ArrayList<>(Collections.singleton("COMMIT")))
                    .token(event.getToken())
                    .timestamp(System.currentTimeMillis())
                    .build();
            System.out.println("updateReadModelEvent: " + updateReadModelEvent);
            // Save outbox
            this.outBoxRepository.save(OutBoxMessage.builder().eventType(eventType).payload(new ObjectMapper().writeValueAsString(updateReadModelEvent)).build());
            System.out.println("COMMIT FOR REGISTER THREAD");
    }

    private boolean validateDuplicateCommitEvent(CommitEvent event){
        try {
            this.transactionLogRepository.save(TransactionLog.builder().correlationId(event.getCorrelationId()).status("COMMIT").build());
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return  true;
        }
    }

    private boolean validateDuplicateRollbackEvent(RollBackEvent event){
        try {
            this.transactionLogRepository.save(TransactionLog.builder().correlationId(event.getCorrelationId()).status("ROLLBACK").build());
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return  true;
        }
    }


    private boolean validateDuplicateEvent(ReserveSlotEvent event){
        try {
            this.transactionLogRepository.save(TransactionLog.builder().correlationId(event.getCorrelationId()).status("START_TRANSACTION").build());
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return  true;
        }
    }

    private void add(List<Long> addCourses, Long studentId, String token)  throws  Exception{
        for(int i = 0; i < addCourses.size(); i++){
            Long courseId = addCourses.get(i);
            while (true) {
                try {
                    Optional<Enrollment> freeSlot = this.enrollmentRepository.findFirstByCourseIdAndStatus(courseId, "PENDING");
                    if(!freeSlot.isPresent()){
                        Course course = (Course) this.serviceAPI.call(
                                this.courseServiceUrl + "/course/" + courseId,
                                HttpMethod.GET,
                                null,
                                Course.class,
                                token
                        );
                        throw new Exception(course.getCode()  + " IS FULL");
                    }
                    //save
                    Enrollment freeEnrollment = freeSlot.get();
                    this.enrollmentRepository.delete(freeEnrollment);

                    this.enrollmentRepository.save(Enrollment.builder().
                            studentId(studentId).
                            courseId(courseId).
                            status("REGISTERED").
                            orderNumber(freeEnrollment.getOrderNumber()).
                            isActive(true)
                            .build());
                    break;
                } catch (Exception e) {
                    // exception occur when two threads try to reserve the same slot
                    // ->> continue find slot, no worry
                    if(e.getMessage().contains("FULL")){
                        System.out.println("FULL");
                        throw e;
                    }
                    else System.out.println("continue find slot");
                }
            }
        }
    }

    private void delete(List<Long> deleteCourses, Long studentId) {
        for(int i = 0 ; i< deleteCourses.size(); i++){
            Long courseId = deleteCourses.get(i);
            Enrollment enrollment = this.enrollmentRepository.findOneByStudentIdAndCourseId(studentId, courseId);
            enrollment.setStudentId(null);
            enrollment.setStatus("PENDING");
            this.enrollmentRepository.save(enrollment);
        }
    }
}