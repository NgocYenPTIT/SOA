package com.example.subject_service.repository;

import com.example.subject_service.model.RegisterSubjectView;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterSubjectRepository extends MongoRepository<RegisterSubjectView, String> {
    List<RegisterSubjectView> findByStudentId(Long studentId);
}