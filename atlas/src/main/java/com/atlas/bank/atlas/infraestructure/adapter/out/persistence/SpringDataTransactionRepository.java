package com.atlas.bank.atlas.infraestructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataTransactionRepository extends JpaRepository<TransactionJpaEntity, Long> {

    List<TransactionJpaEntity> findBySourceAccountIdOrTargetAccountId(Long sourceId, Long targetId);
}
