package com.atlas.bank.atlas.service;

import com.atlas.bank.atlas.model.Account;

import java.util.List;

public interface IAccountService {
    Account create(Account account);

    List<Account> findAll();

    Account findById(Long id);
}
