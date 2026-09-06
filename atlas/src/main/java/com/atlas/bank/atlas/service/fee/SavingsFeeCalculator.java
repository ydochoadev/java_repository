package com.atlas.bank.atlas.service.fee;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SavingsFeeCalculator implements FeeCalculator {
    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(new BigDecimal("0.01"));
    }

    @Override
    public boolean supports(String accountType) {
        return "SAVINGS".equalsIgnoreCase(accountType);
    }
}
