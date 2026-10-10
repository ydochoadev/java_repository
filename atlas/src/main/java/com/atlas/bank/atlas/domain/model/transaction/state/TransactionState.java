package com.atlas.bank.atlas.domain.model.transaction.state;

import com.atlas.bank.atlas.domain.model.transaction.TransactionStatus;

public sealed interface TransactionState permits
        PendingState,
        ValidatedState,
        ExecutedState,
        RejectedState,
        ReversedState {
    TransactionStatus status();

    default TransactionState validate() {
        throw new IllegalStateException("No se puede ejecutar una transacción en estado " + status());
    }

    default TransactionState execute() {
        throw new IllegalStateException("No se puede ejecutar una transacción en estado " + status());
    }

    default TransactionState reject(String reason) {
        throw new IllegalStateException("No se puede ejecutar una transacción en estado " + status());
    }

    default TransactionState reverse() {
        throw new IllegalStateException("No se puede ejecutar una transacción en estado " + status());
    }
}
