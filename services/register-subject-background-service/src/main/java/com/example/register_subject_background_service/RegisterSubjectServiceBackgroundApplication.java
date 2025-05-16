package com.example.register_subject_background_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RegisterSubjectServiceBackgroundApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegisterSubjectServiceBackgroundApplication.class, args);
        System.out.println("hi from  Register Subject Background Service");
    }

}
