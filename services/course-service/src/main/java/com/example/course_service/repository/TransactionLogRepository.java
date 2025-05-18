package com.example.course_service.repository;

import com.example.course_service.model.Course;
import com.example.course_service.model.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
}