package com.example.enrollment_service.repository;

import com.example.enrollment_service.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentIdAndDeletedAtIsNullAndStatus(Long id, String status);

    Enrollment findOneByStudentIdAndCourseId(Long studentId, Long courseId);

    Optional<Enrollment> findFirstByCourseIdAndStatus(Long courseId, String status);
}