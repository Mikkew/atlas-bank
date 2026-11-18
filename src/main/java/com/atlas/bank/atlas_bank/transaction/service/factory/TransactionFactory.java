package com.atlas.bank.atlas_bank.transaction.service.factory;

import com.atlas.bank.atlas_bank.transaction.model.Transaction;
import com.atlas.bank.atlas_bank.transaction.model.TransactionStatus;
import com.atlas.bank.atlas_bank.transaction.model.TransactionTyoe;
import com.atlas.bank.atlas_bank.transaction.service.transfer.TransferContext;

import java.math.BigDecimal;

public class TransactionFactory {

    public static Transaction createTransfer(TransferContext ctx, BigDecimal fee) {
        return Transaction.builder()
                .type(TransactionTyoe.TRANSFER)
                .sourceAccountId(ctx.from().getId())
                .targetAccountId(ctx.to().getId())
                .amount(ctx.amount())
                .fee(fee)
                .status(TransactionStatus.EXECUTED)
                .build();
    }
}
