package com.atlas.bank.atlas.domain.exepcion;

public class FraudCheckException extends RuntimeException {
    public FraudCheckException(String reason) {
        super(reason);
    }
}
