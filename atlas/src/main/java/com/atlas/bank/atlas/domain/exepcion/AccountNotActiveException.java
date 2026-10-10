package com.atlas.bank.atlas.domain.exepcion;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException(Long accountId, String status) {
        super("La cuenta " + accountId + " no está activa. Estado actual: " + status);
    }
}
