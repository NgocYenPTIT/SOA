package com.example.read_model.repository;

import com.example.read_model.model.RegisterSubjectView;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterSubjectRepository extends MongoRepository<RegisterSubjectView, Long> {
    List<RegisterSubjectView> findByStudentId(Long studentId);
}