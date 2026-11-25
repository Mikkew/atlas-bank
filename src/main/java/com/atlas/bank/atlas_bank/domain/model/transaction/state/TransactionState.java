package com.atlas.bank.atlas_bank.domain.model.transaction.state;

import com.atlas.bank.atlas_bank.domain.model.transaction.TransactionStatus;

public sealed interface TransactionState permits PendingState, ValidateState, ExecutedState, RejectedState, ReversedState {

    TransactionStatus status();

    default TransactionState validate() {
        throw new IllegalStateException("No se puede ejecutar en estado " + status());
    }

    default TransactionState execute() {
        throw new IllegalStateException("No se puede ejecutar en estado " + status());
    }

    default TransactionState reject() {
        throw new IllegalStateException("No se puede ejecutar en estado " + status());
    }

    default TransactionState reverse() {
        throw new IllegalStateException("No se puede ejecutar en estado " + status());
    }
}
