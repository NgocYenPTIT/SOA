package com.example.enrollment_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "transaction_log")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionLog {
    @Id
    private String correlationId;
}
