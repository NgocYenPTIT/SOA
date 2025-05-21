package com.example.read_model.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "register_subject_view")
public class RegisterSubjectView {
    private Long id;
    private String semester ;//= "1";
    private String year;// = "2024-2025";
    private String endOfEnrollmentTime ;//= "00:00:00 01-01-2026";
    private  Integer numOfRegisteredSubject;
    private  Integer numOfRegisteredCredit;
    private  List<OpeningSubject> openSubject;
    private List<RegisteredSubject> registeredSubject;

}
