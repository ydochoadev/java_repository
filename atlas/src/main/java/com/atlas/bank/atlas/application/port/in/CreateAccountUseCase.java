package com.atlas.bank.atlas.application.port.in;


import com.atlas.bank.atlas.domain.model.account.Account;

public interface CreateAccountUseCase {
    Account create(Account account);
}
