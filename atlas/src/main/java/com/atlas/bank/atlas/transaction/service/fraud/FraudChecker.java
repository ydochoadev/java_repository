package com.atlas.bank.atlas.transaction.service.fraud;

import com.atlas.bank.atlas.domain.model.shared.FraudCheckResult;

import java.math.BigDecimal;

public interface FraudChecker {
    FraudCheckResult check(Long accountId, BigDecimal amount);
}
