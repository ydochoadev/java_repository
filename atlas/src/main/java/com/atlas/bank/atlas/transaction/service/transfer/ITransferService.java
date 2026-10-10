package com.atlas.bank.atlas.transaction.service.transfer;

import com.atlas.bank.atlas.domain.model.transaction.Transaction;

import java.math.BigDecimal;

public interface ITransferService {
    Transaction execute(Long fromId, Long toId, BigDecimal amount);
}
