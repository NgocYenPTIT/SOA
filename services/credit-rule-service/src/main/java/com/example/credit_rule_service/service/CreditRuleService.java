package com.example.credit_rule_service.service;

import com.example.credit_rule_service.model.CreditRule;
import com.example.credit_rule_service.repository.CreditRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditRuleService {

    private final CreditRuleRepository userRepository;

    @Autowired
    public CreditRuleService(CreditRuleRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<CreditRule> getCreditRulesBySemesterId(Long semesterId) {
        return userRepository.findBySemesterId(semesterId);
    }
}