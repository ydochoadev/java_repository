package com.atlas.bank.atlas.transaction.model.state;

import com.atlas.bank.atlas.transaction.model.TransactionStatus;

public record PendingState() implements TransactionState {
    @Override
    public TransactionStatus status() {
        return TransactionStatus.PENDING;
    }
}
