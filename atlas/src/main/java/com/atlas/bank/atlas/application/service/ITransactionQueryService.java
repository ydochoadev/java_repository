package com.atlas.bank.atlas.application.service;

import com.atlas.bank.atlas.domain.model.transaction.Transaction;

import java.util.List;

public interface ITransactionQueryService {
    List<Transaction> getByAccountId(Long accountId);
}
