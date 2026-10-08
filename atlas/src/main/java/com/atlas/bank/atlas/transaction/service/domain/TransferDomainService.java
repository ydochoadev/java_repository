package com.atlas.bank.atlas.transaction.service.domain;


import com.atlas.bank.atlas.domain.model.account.Account;
import com.atlas.bank.atlas.domain.model.shared.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferDomainService {

    public void transfer(Account fromAccount, Account toAccount, BigDecimal amount, BigDecimal fee) {
        Money totalDebit = Money.of(amount.add(fee), fromAccount.getBalance().getCurrency());
        Money depositAmount = Money.of(amount, toAccount.getBalance().getCurrency());
        fromAccount.withdraw(totalDebit);
        toAccount.deposit(depositAmount);
    }
}
