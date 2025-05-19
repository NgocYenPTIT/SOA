package com.example.course_background_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CourseServiceBackgroundApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseServiceBackgroundApplication.class, args);
        System.out.println("hi from  Course Background Service");
    }

}
