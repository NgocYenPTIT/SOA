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
                subject1.setSubjectCode("MATH101");
                subject1.setSubjectName("Calculus I");
                subject1.setCredit(3);
                subject1.setDescription("Introduction to calculus concepts");
                subject1.setPrerequisiteSubjects("None");
                subjectRepository.save(subject1);

                Subject subject2 = new Subject();
                subject2.setSubjectCode("PHYS101");
                subject2.setSubjectName("Physics I");
                subject2.setCredit(4);
                subject2.setDescription("Fundamental physics principles");
                subject2.setPrerequisiteSubjects("MATH101");
                subjectRepository.save(subject2);

                Subject subject3 = new Subject();
                subject3.setSubjectCode("CHEM101");
                subject3.setSubjectName("Chemistry I");
                subject3.setCredit(3);
                subject3.setDescription("Basic chemical reactions and concepts");
                subject3.setPrerequisiteSubjects("None");
                subjectRepository.save(subject3);

                Subject subject4 = new Subject();
                subject4.setSubjectCode("CS101");
                subject4.setSubjectName("Introduction to Computer Science");
                subject4.setCredit(3);
                subject4.setDescription("Basics of programming and algorithms");
                subject4.setPrerequisiteSubjects("MATH101");
                subjectRepository.save(subject4);

                Subject subject5 = new Subject();
                subject5.setSubjectCode("BIO101");
                subject5.setSubjectName("Biology I");
                subject5.setCredit(4);
                subject5.setDescription("Fundamental biological processes");
                subject5.setPrerequisiteSubjects("CHEM101");
                subjectRepository.save(subject5);

                System.out.println("Đã khởi tạo dữ liệu mẫu cho các môn học");
            }
        };
    }
}
