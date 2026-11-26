package com.atlas.bank.atlas_bank.infrastructure.adapter.out.persistence.repository;

import com.atlas.bank.atlas_bank.infrastructure.adapter.out.persistence.entity.CustomerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerJpaEntity, Long> {
}
