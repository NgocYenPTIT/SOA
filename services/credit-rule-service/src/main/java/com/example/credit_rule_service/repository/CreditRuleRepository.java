package com.example.credit_rule_service.repository;

import com.example.credit_rule_service.model.CreditRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRuleRepository extends JpaRepository<CreditRule, Long> {
}