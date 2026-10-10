package com.atlas.bank.atlas.infraestructure.adapter.out.persistence;

import com.atlas.bank.atlas.application.port.out.TransactionRepositoryPort;
import com.atlas.bank.atlas.domain.model.transaction.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaTransactionRepositoryAdapter implements TransactionRepositoryPort {
    @Override
    public Transaction save(Transaction transaction) {
        return null;
    }

    @Override
    public List<Transaction> findBySourceAccountIdOrTargetAccountId(Long sourceId, Long targetId) {
        return List.of();
    }
}
