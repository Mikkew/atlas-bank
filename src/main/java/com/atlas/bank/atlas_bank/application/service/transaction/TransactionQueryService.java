package com.atlas.bank.atlas_bank.application.service.transaction;

import com.atlas.bank.atlas_bank.application.port.in.GetTransactionByAccountUseCase;
import com.atlas.bank.atlas_bank.application.port.out.TransactionRepositoryPort;
import com.atlas.bank.atlas_bank.domain.model.transaction.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionQueryService implements GetTransactionByAccountUseCase {

    private final TransactionRepositoryPort transactionRepository;

    @Override
    public List<Transaction> getByAccountById(Long accountId) {
        return transactionRepository
                .findBySourceAccountIdOrTargetAccountId(accountId, accountId);
    }
}
