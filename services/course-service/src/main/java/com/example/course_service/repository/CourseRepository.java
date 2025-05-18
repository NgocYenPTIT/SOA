package com.example.course_service.repository;

import com.example.course_service.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllBySemesterIdAndDeletedAtIsNull(Long semesterId);

    @Modifying
    @Query("UPDATE Course c SET " +
            "c.currentStudents = c.currentStudents + 1, " +
            "c.remainSlot = c.remainSlot - 1, " +
            "c.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE c.id = :courseId")
    int incrementCurrentStudents(@Param("courseId") Long courseId);

    @Modifying
    @Query("UPDATE Course c SET " +
            "c.currentStudents = c.currentStudents - 1, " +
            "c.remainSlot = c.remainSlot + 1, " +
            "c.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE c.id = :courseId")
    int decrementCurrentStudents(@Param("courseId") Long courseId);
}