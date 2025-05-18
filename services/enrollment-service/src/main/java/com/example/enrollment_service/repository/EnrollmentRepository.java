package com.example.enrollment_service.repository;

import com.example.enrollment_service.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentIdAndDeletedAtIsNullAndStatus(Long id, String status);

    List<Enrollment> findByCourseIdAndStatus(Long courseId, String status);

    Optional<Enrollment> findOneByCourseIdAndStatus(Long courseId, String pending);

    Optional<Enrollment> findOneByCourseIdAndStatusAndStudentId(Long courseId, String registered, Long studentId);

    Enrollment findByStudentIdAndCourseId(Long studentId, Long courseId);
}