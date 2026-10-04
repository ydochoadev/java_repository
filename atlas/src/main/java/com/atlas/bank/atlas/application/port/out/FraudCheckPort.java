package com.atlas.bank.atlas.application.port.out;

import com.atlas.bank.atlas.transaction.service.fraud.FraudCheckResult;

import java.math.BigDecimal;

public interface FraudCheckPort {
    FraudCheckResult check(Long accountId, BigDecimal amount);
}
