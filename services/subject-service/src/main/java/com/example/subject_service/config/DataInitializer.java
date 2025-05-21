package com.example.subject_service.config;

import com.example.subject_service.model.Subject;
import com.example.subject_service.repository.SubjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(SubjectRepository subjectRepository) {
        return args -> {
            if (subjectRepository.count() == 0) {
                // Seed sample subjects
                Subject subject1 = new Subject();
                subject1.setSubjectCode("INT123");
                subject1.setSubjectName("C Programing");
                subject1.setCredit(1);
                subject1.setDescription("Introduction to C");
                subject1.setPrerequisiteSubjects("None");
                subjectRepository.save(subject1);

                Subject subject2 = new Subject();
                subject2.setSubjectCode("INT456");
                subject2.setSubjectName("Java Programing");
                subject2.setCredit(2);
                subject2.setDescription("Introduction to Java");
                subject2.setPrerequisiteSubjects("MATH101");
                subjectRepository.save(subject2);

                Subject subject3 = new Subject();
                subject3.setSubjectCode("INT789");
                subject3.setSubjectName("Python Programing");
                subject3.setCredit(3);
                subject3.setDescription("Introduction to Python");
                subject3.setPrerequisiteSubjects("None");
                subjectRepository.save(subject3);

                Subject subject4 = new Subject();
                subject4.setSubjectCode("INT888");
                subject4.setSubjectName("OOP Programing");
                subject4.setCredit(4);
                subject4.setDescription("Basics of programming and algorithms");
                subject4.setPrerequisiteSubjects("MATH101");
                subjectRepository.save(subject4);

            }
        };
    }
}
