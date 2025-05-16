package com.example.course_service.repository;

import com.example.course_service.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllBySemesterIdAndDeletedAtIsNull(Long semesterId);
}