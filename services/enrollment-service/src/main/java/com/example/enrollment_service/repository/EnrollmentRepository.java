package com.example.enrollment_service.repository;

import com.example.enrollment_service.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentIdAndDeletedAtIsNullAndStatus(Long id, String status);
}