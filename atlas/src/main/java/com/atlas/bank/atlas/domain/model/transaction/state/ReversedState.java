package com.atlas.bank.atlas.domain.model.transaction.state;

import com.atlas.bank.atlas.domain.model.transaction.TransactionStatus;

public record ReversedState() implements TransactionState {
    @Override
    public TransactionStatus status() {
        return TransactionStatus.REVERSED;
    }
}
