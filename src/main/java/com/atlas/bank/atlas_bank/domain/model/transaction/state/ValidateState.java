package com.atlas.bank.atlas_bank.domain.model.transaction.state;

import com.atlas.bank.atlas_bank.domain.model.transaction.TransactionStatus;

public record ValidateState() implements TransactionState {

    @Override
    public TransactionStatus status() {
        return TransactionStatus.VALIDATED;
    }

    @Override
    public TransactionState execute() {
        return new ExecutedState();
    }

    @Override
    public TransactionState reject() {
        return new RejectedState();
    }
}
