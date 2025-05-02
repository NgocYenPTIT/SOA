package com.example.semester_service.service;

import com.example.semester_service.model.Semester;
import com.example.semester_service.repository.SemesterRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SemesterService {

    private final SemesterRepository semesterRepository;

    @Autowired
    public SemesterService(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    public Semester findSemesterById(Long id) {
        Optional<Semester> semesterOptional = semesterRepository.findById(id);
        return semesterOptional.orElse(null);
    }

}