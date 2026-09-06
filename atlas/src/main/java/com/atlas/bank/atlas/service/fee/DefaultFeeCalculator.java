package com.atlas.bank.atlas.service.fee;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DefaultFeeCalculator implements FeeCalculator {
    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return BigDecimal.ZERO;
    }

    @Override
    public boolean supports(String accountType) {
        return true;
    }
}
