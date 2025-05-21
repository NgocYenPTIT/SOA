package com.example.enrollment_service.repository;

import com.example.enrollment_service.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentIdAndDeletedAtIsNullAndStatus(Long id, String status);

    Enrollment findOneByStudentIdAndCourseId(Long studentId, Long courseId);

    Optional<Enrollment> findFirstByCourseIdAndStatus(Long courseId, String status);

    List<Enrollment> findByCourseId(Long courseId);


    List<Enrollment> findByCourseIdInAndDeletedAtIsNull(List<Long> courseIds);

    List<Enrollment> findByCourseIdInAndDeletedAtIsNullAndStudentIdIsNullAndStatus(List<Long> courseIds, String pending);
}