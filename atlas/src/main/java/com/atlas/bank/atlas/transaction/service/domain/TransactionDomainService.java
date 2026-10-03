package com.atlas.bank.atlas.transaction.service.domain;

import com.atlas.bank.atlas.account.model.Account;
import com.atlas.bank.atlas.shared.model.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionDomainService {

    public void transfer(Account fromAccount, Account toAccount, BigDecimal amount, BigDecimal fee) {
        Money totalDebit = Money.of(amount.add(fee), fromAccount.getBalance().getCurrency());
        Money depositAmount = Money.of(amount, toAccount.getBalance().getCurrency());
        fromAccount.withdraw(totalDebit);
        toAccount.deposit(depositAmount);
    }
}
