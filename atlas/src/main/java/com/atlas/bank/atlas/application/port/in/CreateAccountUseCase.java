package com.atlas.bank.atlas.application.port.in;

import com.atlas.bank.atlas.account.model.Account;

public interface CreateAccountUseCase {
    Account execute(Account account);
}
