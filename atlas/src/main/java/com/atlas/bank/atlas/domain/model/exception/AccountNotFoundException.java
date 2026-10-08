package com.atlas.bank.atlas.domain.model.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super("No se encontró la cuenta con id: " + id);
    }
}
