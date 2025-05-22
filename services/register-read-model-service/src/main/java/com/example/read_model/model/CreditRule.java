package com.example.read_model.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRule {
    private Long id;
    private Integer minCredits;
    private Integer maxCredits;

    private Long semesterId;

    private boolean isActive = true;

    private Date createdAt;
    private Date updatedAt;

    private Date deletedAt;

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}