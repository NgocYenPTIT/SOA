package com.example.credit_rule_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.credit_rule_service.model.CreditRule;
import com.example.credit_rule_service.repository.CreditRuleRepository;
import com.example.credit_rule_service.security.JwtTokenProvider;

@RestController
public class CreditRuleController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CreditRuleRepository creditRuleRepository;

    public CreditRuleController(JwtTokenProvider jwtTokenProvider,CreditRuleRepository creditRuleRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.creditRuleRepository = creditRuleRepository;
    }
    
    @GetMapping("/credit-rule/semester/{id}")
    public ResponseEntity<CreditRule> getCreditRulesBySemesterId(@PathVariable("id") Long semesterId) {
        CreditRule creditRule = creditRuleRepository.findBySemesterId(semesterId);
        System.out.println("xin chao");
        return ResponseEntity.ok(creditRule);
    }
}