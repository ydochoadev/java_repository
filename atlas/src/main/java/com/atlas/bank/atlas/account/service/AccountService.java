package com.atlas.bank.atlas.account.service;

import com.atlas.bank.atlas.application.port.out.AccountRepositoryPort;
import com.atlas.bank.atlas.domain.model.account.Account;
import com.atlas.bank.atlas.domain.model.exception.AccountNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepositoryPort accountRepository;

    @Override
    @Transactional
    public Account create(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "accounts", key = "#id")
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }
}
