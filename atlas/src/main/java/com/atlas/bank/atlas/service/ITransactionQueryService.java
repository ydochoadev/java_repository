package com.atlas.bank.atlas.service;

import com.atlas.bank.atlas.model.Transaction;

import java.util.List;

public interface ITransactionQueryService {
    List<Transaction> getByAccountId(Long accountId);
}
