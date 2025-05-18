package com.example.credit_rule_service.config;

import com.example.credit_rule_service.model.CreditRule;
import com.example.credit_rule_service.repository.CreditRuleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(CreditRuleRepository creditRuleRepository) {
        return args -> {
            // Khởi tạo quy định tín chỉ (CreditRules)
            if (creditRuleRepository.count() == 0) {
                System.out.println("Bắt đầu khởi tạo dữ liệu quy định tín chỉ...");

                // Quy định cho học kỳ 1
                CreditRule rule1 = new CreditRule();
                rule1.setSemesterId(1L); // Học kỳ 1
                rule1.setMinCredits(3);
                rule1.setMaxCredits(10);
                creditRuleRepository.save(rule1);


                System.out.println("Đã khởi tạo " + creditRuleRepository.count() + " quy định tín chỉ vào database");
            }
        };
    }
}