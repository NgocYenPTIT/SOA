package com.example.register_subject_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RegisterSubjectServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegisterSubjectServiceApplication.class, args);
        System.out.println("hi from  Register Subject Service");
    }

}
