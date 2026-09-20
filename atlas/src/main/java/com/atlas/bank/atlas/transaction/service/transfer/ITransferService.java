package com.atlas.bank.atlas.transaction.service;

import com.atlas.bank.atlas.transaction.model.Transaction;

import java.math.BigDecimal;

public interface ITransferService {
    Transaction execute(Long fromId, Long toId, BigDecimal amount);
}
