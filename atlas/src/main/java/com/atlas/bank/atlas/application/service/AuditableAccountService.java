package com.atlas.bank.atlas.application.service;

import com.atlas.bank.atlas.application.port.in.CreateAccountUseCase;
import com.atlas.bank.atlas.application.port.in.GetAccountUseCase;
import com.atlas.bank.atlas.application.port.in.ListAccountsUseCase;
import com.atlas.bank.atlas.domain.model.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Primary
public class AuditableAccountService implements CreateAccountUseCase, ListAccountsUseCase, GetAccountUseCase {

    private final CreateAccountUseCase createAccountUseCase;
    private final ListAccountsUseCase listAccountsUseCase;
    private final GetAccountUseCase getAccountUseCase;

    public AuditableAccountService(CreateAccountUseCase createAccountUseCase,
                                   ListAccountsUseCase listAccountsUseCase,
                                   GetAccountUseCase getAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.listAccountsUseCase = listAccountsUseCase;
        this.getAccountUseCase = getAccountUseCase;
    }

    @Override
    public Account create(Account account) {
        log.info("Creando cuenta - número {}, titular: {}", account.getAccountNumber(), account.getOwnerName());
        Account created = createAccountUseCase.create(account);
        log.info("Cuenta creada exitosamente - ID: {}", account.getId());

        return created;
    }

    @Override
    public List<Account> findAll() {
        return listAccountsUseCase.findAll();
    }

    @Override
    public Account findById(Long id) {
        return getAccountUseCase.findById(id);
    }
}
