package com.atlas.bank.atlas.infraestructure.adapter.out.persistence;

import com.atlas.bank.atlas.application.port.out.AccountRepositoryPort;
import com.atlas.bank.atlas.domain.model.account.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JpaAccountRepositoryAdapter implements AccountRepositoryPort {

    private final SpringDataAccountrepository repository;
    private final AccountPersistenceMapper mapper;

    @Override
    public Optional<Account> findById(Long id) {
        return this.repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Account> findAll() {
        return this.repository.findAll()
                .stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public Account save(Account account) {
        account.initDefaults();
        AccountJpaEntity entity = mapper.toJpaEntity(account);
        AccountJpaEntity saved = this.repository.save(entity);
        return mapper.toDomain(saved);
    }
}
