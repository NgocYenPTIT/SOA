package com.example.enrollment_service.service;

import com.example.enrollment_service.model.*;
import com.example.enrollment_service.repository.EnrollmentRepository;
import com.example.enrollment_service.repository.OutBoxRepository;
import com.example.enrollment_service.repository.TransactionLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EnrollmentService {

    private  EnrollmentRepository enrollmentRepository;
    private TransactionLogRepository transactionLogRepository;
    private OutBoxRepository outBoxRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, TransactionLogRepository transactionLogRepository, OutBoxRepository outBoxRepository) {
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
               List<Long> deleteCourses = addAndDeleteCourses.get(1);

               add(addCourses, studentId);
               delete(deleteCourses, studentId);

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
               //emit FULL
               e.printStackTrace();
               System.out.println("RESERVE FULL");
               throw e;
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

    private void add(List<Long> addCourses, Long studentId)  throws  Exception{
        for(int i = 0; i < addCourses.size(); i++){
            Long courseId = addCourses.get(i);
            while (true) {
                try {
                    Optional<Enrollment> freeSlot = this.enrollmentRepository.findFirstByCourseIdAndStatus(courseId, "PENDING");
                    if(!freeSlot.isPresent()){
                        throw new Exception("FULL");
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
                    if(e.getMessage().equals("FULL")){
                        System.out.println("FULL");
                        throw e;
                    }
//                    e.printStackTrace();
                    System.out.println("continue find slot");
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