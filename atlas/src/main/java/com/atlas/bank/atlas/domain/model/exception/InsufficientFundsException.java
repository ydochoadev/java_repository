package com.atlas.bank.atlas.domain.model.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(Long accountId, BigDecimal balance, BigDecimal amount) {
        super("La cuenta " + accountId + " tiene un saldo de " + balance + "y se intentó transferir " + amount);
    }
}
