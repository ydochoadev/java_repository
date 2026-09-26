package com.atlas.bank.atlas.transaction.service.fee;

import com.atlas.bank.atlas.account.model.AccountType;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(1)
public class SavingsFeeCalculator implements FeeCalculator {
    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(new BigDecimal("0.01"));
    }

    @Override
    public boolean supports(AccountType accountType) {
        return accountType == AccountType.PREMIUM;
    }
}
