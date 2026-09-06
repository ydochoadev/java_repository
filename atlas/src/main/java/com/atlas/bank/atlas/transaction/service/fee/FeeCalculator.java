package com.atlas.bank.atlas.transaction.service.fee;

import java.math.BigDecimal;

public interface FeeCalculator {
    boolean supports(String accountType);

    BigDecimal calculate(BigDecimal amount);
}
