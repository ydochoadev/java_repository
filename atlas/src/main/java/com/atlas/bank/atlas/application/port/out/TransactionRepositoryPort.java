package com.atlas.bank.atlas.application.port.out;

import com.atlas.bank.atlas.domain.model.transaction.Transaction;

import java.util.List;

public interface TransactionRepositoryPort {

    Transaction save(Transaction transaction);

    List<Transaction> findBySourceAccountIdOrTargetAccountId(Long sourceId, Long targetId);
}
