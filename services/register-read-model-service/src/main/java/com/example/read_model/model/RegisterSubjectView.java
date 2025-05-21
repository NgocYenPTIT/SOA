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
    private String semester ;
    private String year;
    private String endOfEnrollmentTime ;
    private Integer numOfRegisteredSubject;
    private Integer numOfRegisteredCredit;
    private List<OpeningSubject> openSubject;
    private List<RegisteredSubject> registeredSubject;
    private String status;
    private List<String> messages;
    private Long lastUpdate;
}
