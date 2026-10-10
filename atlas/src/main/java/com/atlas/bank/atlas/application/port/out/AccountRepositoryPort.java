package com.atlas.bank.atlas.application.port.out;

import com.atlas.bank.atlas.domain.model.account.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepositoryPort {
    Optional<Account> findById(Long id);

    List<Account> findAll();

    Account save(Account account);
}
