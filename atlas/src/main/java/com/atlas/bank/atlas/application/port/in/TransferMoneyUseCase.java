package com.atlas.bank.atlas.application.port.in;

import com.atlas.bank.atlas.domain.model.transaction.Transaction;

import java.math.BigDecimal;

public interface TransferMoneyUseCase {
    Transaction execute(Long fromId, Long toId, BigDecimal amount);
}
