package com.atlas.bank.atlas_bank.application.service.transaction;

import com.atlas.bank.atlas_bank.domain.model.transaction.Transaction;

import java.util.List;

public interface ITransactionQueryService {

    List<Transaction> getByAccountById(Long accountId);
}
