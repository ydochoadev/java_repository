package com.atlas.bank.atlas.application.port.in;

import com.atlas.bank.atlas.domain.model.transaction.Transaction;

import java.util.List;

public interface GetTransactionsByAccountUseCase {
    List<Transaction> getByAccountId(Long accountId);
}
