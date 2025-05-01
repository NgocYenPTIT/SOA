package com.example.semester_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SemesterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SemesterServiceApplication.class, args);
        System.out.println("hi from Semester Service");
    }

}
