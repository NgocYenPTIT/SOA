package com.example.wish_subject_service.repository;

import com.example.wish_subject_service.model.WishSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishSubjectRepository extends JpaRepository<WishSubject, Long> {
    List<WishSubject> findByStudentId(Long studentId);
}