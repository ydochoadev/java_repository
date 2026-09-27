package com.atlas.bank.atlas.transaction.model.state;

import com.atlas.bank.atlas.transaction.model.TransactionStatus;

public record ValidatedState() implements TransactionState {
    @Override
    public TransactionStatus status() {
        return TransactionStatus.VALIDATED;
    }

    @Override
    public TransactionState execute() {
        return new ExecutedState();
    }

    @Override
    public TransactionState reject(String reason) {
        return new RejectedState();
    }
}
