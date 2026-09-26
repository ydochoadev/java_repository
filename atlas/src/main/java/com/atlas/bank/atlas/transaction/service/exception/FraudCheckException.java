package com.atlas.bank.atlas.transaction.service.exception;

public class FraudCheckException extends RuntimeException {
    public FraudCheckException(String reason) {
        super(reason);
    }
}
