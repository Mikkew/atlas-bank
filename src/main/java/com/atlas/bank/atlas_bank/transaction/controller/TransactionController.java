package com.atlas.bank.atlas_bank.transaction.controller;

import com.atlas.bank.atlas_bank.transaction.dto.TransactionResponse;
import com.atlas.bank.atlas_bank.transaction.dto.TransferRequest;
import com.atlas.bank.atlas_bank.transaction.model.Transaction;
import com.atlas.bank.atlas_bank.transaction.service.ITransactionQueryService;
import com.atlas.bank.atlas_bank.transaction.service.ITransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransferService transferService;
    private final ITransactionQueryService transactionQueryService;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody TransferRequest request) {
        final Transaction transaction = transferService.execute(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount());
        return ResponseEntity.ok(toResponse(transaction));
    }

    @GetMapping("/{id}/transaccions")
    public ResponseEntity<List<TransactionResponse>> getTransaccions(@PathVariable Long id) {
        final List<TransactionResponse> responseList = transactionQueryService.getByAccountById(id).stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .sourceAccountId(transaction.getSourceAccountId())
                .targetAccountId(transaction.getTargetAccountId())
                .amount(transaction.getAmount())
                .fee(transaction.getFee())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt()).build();
    }
}
