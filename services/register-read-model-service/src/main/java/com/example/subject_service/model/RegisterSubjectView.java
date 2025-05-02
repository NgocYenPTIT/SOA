package com.example.subject_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "register_subject_view")
@Builder
public class RegisterSubjectView {
    @Id
    private Long studentId;

    private String semester;
    private String year;

    private Date endOfEnrollmentTime;
    //
    private  List<OpeningSubject> openSubject;
    //
    private  Integer numOfRegisteredSubject;
    private  Integer numOfRegisteredCredit;

    //
    private List<RegisteredSubject> registeredSubject;

}
