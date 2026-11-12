package com.atlas.bank.atlas_bank.transaction.service;

import com.atlas.bank.atlas_bank.account.exception.AccountNotFoundException;
import com.atlas.bank.atlas_bank.account.model.Account;
import com.atlas.bank.atlas_bank.transaction.exception.AccountNotActiveException;
import com.atlas.bank.atlas_bank.transaction.exception.InsufficientFundsException;
import com.atlas.bank.atlas_bank.transaction.model.Transaction;
import com.atlas.bank.atlas_bank.account.repository.AccountRepository;
import com.atlas.bank.atlas_bank.transaction.repository.TransactionRepository;
import com.atlas.bank.atlas_bank.transaction.service.fee.FeeCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService implements ITransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final List<FeeCalculator> feeCalculator;

    @Override
    @Transactional
    public Transaction execute(Long fromId, Long toId, BigDecimal amount) {
        // Buscar cuentas
        Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new AccountNotFoundException(fromId));

        Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new AccountNotFoundException(toId));

        // Validar que la cuenta esté activa
        if (!"ACTIVE".equals(from.getStatus())) {
            throw new AccountNotActiveException(fromId, from.getStatus());
        }
        if (!"ACTIVE".equals(to.getStatus())) {
            throw new AccountNotActiveException(toId, to.getStatus());
        }

        // Validar fondos
        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(fromId, from.getBalance(), amount);
        }

        // Calcular comisión — hardcodeada
//        BigDecimal fee;
//        if ("SAVINGS".equals(from.getType())) {
//            fee = amount.multiply(new BigDecimal("0.01"));
//        } else if ("CHECKING".equals(from.getType())) {
//            fee = amount.multiply(new BigDecimal("0.015"));
//        } else {
//            fee = BigDecimal.ZERO;
//        }
        BigDecimal fee = feeCalculator.stream()
                .filter(fc -> fc.supports(from.getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay calculador para el tipo " + from.getType()))
                .calculate(amount);


        // Actualizar saldos
        from.setBalance(from.getBalance().subtract(amount).subtract(fee));
        to.setBalance(to.getBalance().add(amount));
        accountRepository.save(from);
        accountRepository.save(to);

        // Crear transacción
        final Transaction transaction = Transaction.builder()
                .type("TRANSFER")
                .sourceAccountId(fromId)
                .targetAccountId(toId)
                .amount(amount)
                .fee(fee)
                .status("EXECUTED")
                .build();

        return transactionRepository.save(transaction);
    }
}
