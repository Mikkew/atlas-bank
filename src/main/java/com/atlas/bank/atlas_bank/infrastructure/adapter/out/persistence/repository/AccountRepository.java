package com.atlas.bank.atlas_bank.infrastructure.adapter.out.persistence.repository;

import com.atlas.bank.atlas_bank.infrastructure.adapter.out.persistence.entity.AccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository  extends JpaRepository<AccountJpaEntity, Long> {
}
