package com.example.enrollment_service.service;

import com.example.enrollment_service.model.Enrollment;
import com.example.enrollment_service.repository.EnrollmentRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class EnrollmentService {

    private  EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Enrollment> getList(HttpServletRequest request){
        return this.enrollmentRepository.findByStudentIdAndDeletedAtIsNullAndStatus((Long)request.getAttribute("id"), "REGISTERED");
    }
}