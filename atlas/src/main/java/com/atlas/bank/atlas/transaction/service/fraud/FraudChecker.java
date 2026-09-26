package com.atlas.bank.atlas.transaction.service.fraud;

import java.math.BigDecimal;

public interface FraudChecker {
    FraudCheckResult check(Long accountId, BigDecimal amount);
}
