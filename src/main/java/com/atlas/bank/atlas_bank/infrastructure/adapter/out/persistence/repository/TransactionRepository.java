package com.atlas.bank.atlas_bank.infrastructure.adapter.out.persistence.repository;

import com.atlas.bank.atlas_bank.infrastructure.adapter.out.persistence.entity.TransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionJpaEntity, Long> {

    List<TransactionJpaEntity> findBySourceAccountIdOrTargetAccountId(Long sourceId, Long targetId);
}
