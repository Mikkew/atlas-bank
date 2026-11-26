package com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.facade;

import com.atlas.bank.atlas_bank.application.service.account.IAccountService;
import com.atlas.bank.atlas_bank.application.service.transaction.ITransactionQueryService;
import com.atlas.bank.atlas_bank.domain.model.account.Account;
import com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.dto.response.DashboardResponse;
import com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.dto.response.TransactionResponse;
import com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountDashboardFacade {

    private final IAccountService accountService;
    private final ITransactionQueryService transactionQueryService;
    private final TransactionMapper transactionMapper;

    public DashboardResponse getDashboard(Long accountId){

        final Account account = accountService.findById(accountId);

        final List<TransactionResponse> transactions = transactionQueryService
                .getByAccountById(accountId)
                .stream()
                .map(transactionMapper::toResponse)
                .toList();

        return DashboardResponse.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .ownerName(account.getOwnerName())
                .type(account.getType().name())
                .balance(account.getBalance().getAmount())
                .status(account.getStatus().name())
                .recentTransactions(transactions)
                .build();
    }

}
