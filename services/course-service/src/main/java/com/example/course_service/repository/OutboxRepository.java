package com.example.course_service.repository;

import com.example.course_service.model.Course;
import com.example.course_service.model.OutBoxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutBoxMessage, Long> {
}