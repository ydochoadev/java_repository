package com.atlas.bank.atlas.account.service;

import com.atlas.bank.atlas.account.model.Account;

import java.util.List;

public interface IAccountService {
    Account create(Account account);

    List<Account> findAll();

    Account findById(Long id);
}
