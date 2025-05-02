package com.example.wish_subject_service.service;

import com.example.wish_subject_service.repository.WishSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishSubjectService {

    private final WishSubjectRepository userRepository;

    @Autowired
    public WishSubjectService(WishSubjectRepository userRepository) {
        this.userRepository = userRepository;
    }

}