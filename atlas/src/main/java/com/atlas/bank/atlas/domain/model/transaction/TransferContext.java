package com.atlas.bank.atlas.domain.model.transaction;

import com.atlas.bank.atlas.domain.model.account.Account;

import java.math.BigDecimal;

public record TransferContext(Account from, Account to, BigDecimal amount) {
}
