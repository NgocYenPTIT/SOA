package com.example.enrollment_service.repository;

import com.example.enrollment_service.model.OutBoxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutBoxRepository extends JpaRepository<OutBoxMessage, Long> {
}