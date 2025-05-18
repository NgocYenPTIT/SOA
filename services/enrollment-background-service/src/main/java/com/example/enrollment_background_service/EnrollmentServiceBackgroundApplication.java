package com.example.enrollment_background_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EnrollmentServiceBackgroundApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnrollmentServiceBackgroundApplication.class, args);
        System.out.println("hi from  Enrollment Background Service");
    }

}
