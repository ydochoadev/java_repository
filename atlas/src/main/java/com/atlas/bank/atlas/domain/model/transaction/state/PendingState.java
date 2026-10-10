package com.atlas.bank.atlas.domain.model.transaction.state;

import com.atlas.bank.atlas.domain.model.transaction.TransactionStatus;

public record PendingState() implements TransactionState {
    @Override
    public TransactionStatus status() {
        return TransactionStatus.PENDING;
    }

    @Override
    public TransactionState validate() {
        return new ValidatedState();
    }

    @Override
    public TransactionState reverse() {
        return new RejectedState();
    }
}
