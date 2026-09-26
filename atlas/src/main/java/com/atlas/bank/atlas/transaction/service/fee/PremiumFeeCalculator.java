package com.atlas.bank.atlas.transaction.service.fee;

import com.atlas.bank.atlas.account.model.AccountType;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(1)
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
