package com.atlas.bank.atlas.infraestructure.adapter.out.persistence;

import com.atlas.bank.atlas.application.port.out.TransactionRepositoryPort;
import com.atlas.bank.atlas.domain.model.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class JpaTransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final SpringDataTransactionRepository repository;
    private final TransactionPersistenceMapper mapper;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public Transaction save(Transaction transaction) {
        transaction.initDefaults();
        TransactionJpaEntity entity = mapper.toJpaEntity(transaction);
        TransactionJpaEntity saved = repository.save(entity);
        Transaction result = mapper.toDomain(saved);
        // Eventos
        transaction.getDomainEvents().forEach(eventPublisher::publishEvent);
        transaction.clearDomainEvents();

        return result;
    }

    @Override
    public List<Transaction> findBySourceAccountIdOrTargetAccountId(Long sourceId, Long targetId) {
        return repository.findBySourceAccountIdOrTargetAccountId(sourceId, targetId)
                .stream()
                .map(mapper::toDomain).toList();
    }
}
