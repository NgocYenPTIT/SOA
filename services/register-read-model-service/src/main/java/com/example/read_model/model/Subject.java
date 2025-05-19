package com.example.read_model.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    private Long id;

    private String subjectCode;

    private String subjectName;

    private Integer credit;

    private String description;

    private String prerequisiteSubjects;
}
