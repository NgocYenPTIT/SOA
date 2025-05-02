package com.example.clientKTPM.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterSubjectView {
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
