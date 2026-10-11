package com.atlas.bank.atlas.domain.strategy.fee;

import com.atlas.bank.atlas.domain.model.account.AccountType;

import java.math.BigDecimal;

public class CheckingFeeCalculator implements FeeCalculator {
    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(new BigDecimal("0.015"));
    }

    @Override
    public boolean supports(AccountType accountType) {
        return accountType == AccountType.CHECKING;
    }
}
