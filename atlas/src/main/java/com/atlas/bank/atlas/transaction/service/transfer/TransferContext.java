package com.atlas.bank.atlas.transaction.service;

import com.atlas.bank.atlas.account.model.Account;
import org.springframework.transaction.reactive.TransactionContext;

import java.math.BigDecimal;

public record TransferContext(Account from, Account to, BigDecimal amount) {
}
