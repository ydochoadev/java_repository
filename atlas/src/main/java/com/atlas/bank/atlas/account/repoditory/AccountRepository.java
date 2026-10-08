package com.atlas.bank.atlas.account.repoditory;

import com.atlas.bank.atlas.account.model.Account;
import com.atlas.bank.atlas.application.port.out.AccountRepositoryPort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository
        extends JpaRepository<Account, Long>, DomainAccountRepository, AccountRepositoryPort {
}
