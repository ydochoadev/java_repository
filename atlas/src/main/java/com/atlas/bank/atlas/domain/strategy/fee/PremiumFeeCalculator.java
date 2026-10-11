package com.atlas.bank.atlas.domain.strategy.fee;

import com.atlas.bank.atlas.domain.model.account.AccountType;

import java.math.BigDecimal;

public class PremiumFeeCalculator implements FeeCalculator {
    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return BigDecimal.ZERO;
    }

    @Override
    public boolean supports(AccountType accountType) {
        return accountType == AccountType.PREMIUM;
    }
}
