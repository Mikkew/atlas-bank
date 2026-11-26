package com.atlas.bank.atlas_bank.infrastructure.adapter.out.persistence.adapter;

import com.atlas.bank.atlas_bank.application.port.out.TransactionRepositoryPort;
import com.atlas.bank.atlas_bank.domain.model.transaction.Transaction;
import com.atlas.bank.atlas_bank.infrastructure.adapter.out.persistence.entity.TransactionJpaEntity;
import com.atlas.bank.atlas_bank.infrastructure.adapter.out.persistence.mapper.TransactionPersistenceMapper;
import com.atlas.bank.atlas_bank.infrastructure.adapter.out.persistence.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final TransactionRepository transactionRepository;
    private final TransactionPersistenceMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Transaction save(Transaction transaction) {
        transaction.initDefaults();
        TransactionJpaEntity entity = mapper.toJpaEntity(transaction);
        TransactionJpaEntity saved = transactionRepository.save(entity);

        Transaction result = mapper.toDomain(saved);

        transaction.getDomainEvents().forEach(eventPublisher::publishEvent);
        transaction.clearDomainEvents();

        return result;
    }

    @Override
    public List<Transaction> findBySourceAccountIdOrTargetAccountId(Long sourceId, Long targetId) {
        return transactionRepository
                .findBySourceAccountIdOrTargetAccountId(sourceId, sourceId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
