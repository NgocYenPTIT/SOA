package com.example.enrollment_service.service;

import com.example.enrollment_service.model.Enrollment;
import com.example.enrollment_service.model.EnrollmentReserveResponse;
import com.example.enrollment_service.model.ReserveSlotEvent;
import com.example.enrollment_service.model.TransactionLog;
import com.example.enrollment_service.repository.EnrollmentRepository;
import com.example.enrollment_service.repository.TransactionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    private  EnrollmentRepository enrollmentRepository;
    private TransactionLogRepository transactionLogRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, TransactionLogRepository transactionLogRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.transactionLogRepository = transactionLogRepository;
    }

    public List<Enrollment> getList(HttpServletRequest request){
        return this.enrollmentRepository.findByStudentIdAndDeletedAtIsNullAndStatus((Long)request.getAttribute("id"), "REGISTERED");
    }

    @Transactional
    public void reserve(ReserveSlotEvent event) throws Exception {
           boolean isDuplicate = this.validateDuplicateEvent(event);

           if(isDuplicate){
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

           } catch (Exception e) {
               //emit FULL
               e.printStackTrace();
               System.out.println("RESERVE FULL");
               throw e;
           }
    }

    private boolean validateDuplicateEvent(ReserveSlotEvent event){
        try {
            this.transactionLogRepository.save(TransactionLog.builder().correlationId(event.getCorrelationId()).build());
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
                    Optional<Enrollment> freeSlot = this.enrollmentRepository.findOneByCourseIdAndStatus(courseId, "PENDING");
                    if(!freeSlot.isPresent()){
                        throw new Exception("FULL");
                    }
                    //save
                    Enrollment enrollmentToSave = freeSlot.get();
                    enrollmentToSave.setStudentId(studentId);
                    enrollmentToSave.setStatus("REGISTERED");

                    this.enrollmentRepository.save(enrollmentToSave);
                    break;
                } catch (Exception e) {
                    // exception occur when two threads try to reserve the same slot
                    // ->> continue find slot, no worry
                    if(e.getMessage().equals("FULL")){
                        throw e;
                    }
                    e.printStackTrace();
                }
            }
        }
    }

    private void delete(List<Long> deleteCourses, Long studentId) {
        for(int i = 0 ; i< deleteCourses.size(); i++){
            Long courseId = deleteCourses.get(i);
            Enrollment enrollment = this.enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
            enrollment.setStudentId(null);
            enrollment.setStatus("PENDING");
            this.enrollmentRepository.save(enrollment);
        }
    }
}