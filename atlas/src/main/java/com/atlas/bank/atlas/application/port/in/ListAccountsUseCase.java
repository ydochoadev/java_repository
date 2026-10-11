package com.atlas.bank.atlas.application.port.in;

import com.atlas.bank.atlas.domain.model.account.Account;

import java.util.List;

public interface ListAccountsUseCase {
    List<Account> findAll();
}
