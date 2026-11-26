package com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.controller;

import com.atlas.bank.atlas_bank.application.port.in.TransferMoneyUseCase;
import com.atlas.bank.atlas_bank.application.service.transaction.ITransactionQueryService;
import com.atlas.bank.atlas_bank.domain.model.transaction.Transaction;
import com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.dto.request.TransferRequest;
import com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.dto.response.TransactionResponse;
import com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.mapper.TransactionMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

	private final TransferMoneyUseCase transferMoneyUseCase;
    private final ITransactionQueryService transactionQueryService;
    private final TransactionMapper transactionMapper;

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable Long id) {
        final List<TransactionResponse> responseList = transactionQueryService.getByAccountById(id).stream()
                .map(transactionMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        final Transaction transaction = transferMoneyUseCase.execute(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount());
        return ResponseEntity.ok(transactionMapper.toResponse(transaction));
    }
}
