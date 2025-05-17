package com.example.wish_subject_service.service;

import com.example.wish_subject_service.model.WishSubject;
import com.example.wish_subject_service.repository.WishSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishSubjectService {

    private final WishSubjectRepository wishSubjectRepository;

    @Autowired
    public WishSubjectService(WishSubjectRepository wishSubjectRepository) {
        this.wishSubjectRepository = wishSubjectRepository;
    }

    public List<WishSubject> getList(Long studentId) {
        return this.wishSubjectRepository.findByStudentId(studentId);
    }

}