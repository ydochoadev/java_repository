package com.atlas.bank.atlas.service;

import com.atlas.bank.atlas.model.Transaction;

import java.math.BigDecimal;

public interface ITransferService {
    Transaction execute(Long fromId, Long toId, BigDecimal amount);
}
