package com.example.major_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MajorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MajorServiceApplication.class, args);
        System.out.println("hi from Major Service");
    }

}
