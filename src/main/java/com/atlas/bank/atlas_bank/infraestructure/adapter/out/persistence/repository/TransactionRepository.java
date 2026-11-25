package com.atlas.bank.atlas_bank.infraestructure.adapter.out.persistence.repository;

import com.atlas.bank.atlas_bank.infraestructure.adapter.out.persistence.entity.TransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionJpaEntity, Long> {
}
