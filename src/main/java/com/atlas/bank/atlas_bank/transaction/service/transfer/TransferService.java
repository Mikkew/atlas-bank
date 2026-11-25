package com.atlas.bank.atlas_bank.transaction.service.transfer;

import com.atlas.bank.atlas_bank.account.exception.AccountNotFoundException;
import com.atlas.bank.atlas_bank.account.model.Account;
import com.atlas.bank.atlas_bank.application.port.in.TransferMoneyUseCase;
import com.atlas.bank.atlas_bank.application.port.out.AccountRepositoryPort;
import com.atlas.bank.atlas_bank.transaction.model.Transaction;
import com.atlas.bank.atlas_bank.transaction.repository.TransactionRepository;
import com.atlas.bank.atlas_bank.transaction.service.domain.TransferDomainService;
import com.atlas.bank.atlas_bank.transaction.service.factory.TransactionFactory;
import com.atlas.bank.atlas_bank.transaction.service.fee.FeeCalculator;
import com.atlas.bank.atlas_bank.transaction.validation.chain.TransferValidator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService extends TransactionProcessor<TransferContext> 
		implements ITransferService, TransferMoneyUseCase {

    private final AccountRepositoryPort accountRepository;
    private final List<FeeCalculator> feeCalculators;
    private final List<TransferValidator> validators;
    private final TransferDomainService transferDomainService;

    public TransferService(TransactionRepository transactionRepository,
    					   AccountRepositoryPort accountRepository,
                           List<FeeCalculator> feeCalculators,
                           List<TransferValidator> validators,
                           TransferDomainService transferDomainService) {
        super(transactionRepository);
        this.accountRepository = accountRepository;
        this.feeCalculators = feeCalculators;
        this.validators = validators;
        this.transferDomainService = transferDomainService;
    }

    @Override
    @Transactional
    public Transaction execute(Long fromId, Long toId, BigDecimal amount) {
        // Buscar cuentas
        Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new AccountNotFoundException(fromId));

        Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new AccountNotFoundException(toId));

		Transaction transaction = process(new TransferContext(from, to, amount));

        transaction.executedTransfer();

        transactionRepository.save(transaction);

        return transaction;
    }

    @Override
    protected void validate(TransferContext ctx) {
        validators.forEach(validator -> validator.validate(ctx));
    }

    @Override
    protected BigDecimal calculateFee(TransferContext ctx) {
        return feeCalculators.stream()
                .filter(fc -> fc.supports(ctx.from().getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay calculador para el tipo " + ctx.from().getType()))
                .calculate(ctx.amount());
    }

    @Override
    protected void execute(TransferContext ctx, BigDecimal fee) {
        transferDomainService.transfer(ctx.from(), ctx.to(), ctx.amount(), fee);
        accountRepository.save(ctx.from());
        accountRepository.save(ctx.to());
    }

    @Override
    protected Transaction save(TransferContext ctx, BigDecimal fee) {
        return transactionRepository.save(TransactionFactory.createTransfer(ctx, fee));
    }
}
