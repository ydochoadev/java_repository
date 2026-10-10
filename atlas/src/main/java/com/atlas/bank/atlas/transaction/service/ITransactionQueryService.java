package com.atlas.bank.atlas.transaction.service;

import com.atlas.bank.atlas.domain.model.transaction.Transaction;

import java.util.List;

public interface ITransactionQueryService {
    List<Transaction> getByAccountId(Long accountId);
}
