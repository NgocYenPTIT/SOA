package com.example.credit_rule_service.controller;

import com.example.credit_rule_service.security.JwtTokenProvider;
import com.example.credit_rule_service.service.CreditRuleService;
import org.springframework.web.bind.annotation.*;

@RestController
public class CreditRuleController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CreditRuleService creditRuleService;

    public CreditRuleController(JwtTokenProvider jwtTokenProvider, CreditRuleService creditRuleService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.creditRuleService = creditRuleService;
    }
}