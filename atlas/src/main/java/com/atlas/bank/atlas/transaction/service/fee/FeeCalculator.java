package com.atlas.bank.atlas.transaction.service.fee;

import com.atlas.bank.atlas.account.model.AccountType;

import java.math.BigDecimal;

public interface FeeCalculator {
    boolean supports(AccountType accountType);

    BigDecimal calculate(BigDecimal amount);
}
