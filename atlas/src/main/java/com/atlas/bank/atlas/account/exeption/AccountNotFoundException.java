package com.atlas.bank.atlas.account.exeption;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super("No se encontró la cuenta con id: " + id);
    }
}
