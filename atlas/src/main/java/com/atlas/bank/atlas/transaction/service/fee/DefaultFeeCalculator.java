package com.atlas.bank.atlas.transaction.service.fee;

import com.atlas.bank.atlas.account.model.AccountType;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// @Order => Por defecto, se coloca el final de una lista
@Component
@Order()
public class DefaultFeeCalculator implements FeeCalculator {
    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return BigDecimal.ZERO;
    }

    @Override
    public boolean supports(AccountType accountType) {
        return true;
    }
}
