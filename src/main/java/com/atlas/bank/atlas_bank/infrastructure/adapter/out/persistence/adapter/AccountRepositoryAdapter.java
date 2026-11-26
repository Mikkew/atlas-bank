package com.atlas.bank.atlas_bank.infrastructure.adapter.out.persistence.adapter;

import com.atlas.bank.atlas_bank.domain.model.account.Account;
import com.atlas.bank.atlas_bank.application.port.out.AccountRepositoryPort;
import com.atlas.bank.atlas_bank.infrastructure.adapter.out.persistence.entity.AccountJpaEntity;
import com.atlas.bank.atlas_bank.infrastructure.adapter.out.persistence.mapper.AccountPersistenceMapper;
import com.atlas.bank.atlas_bank.infrastructure.adapter.out.persistence.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private final AccountRepository accountRepository;
    private final AccountPersistenceMapper mapper;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Account save(Account account) {
        account.initDefaults();
        AccountJpaEntity entity = mapper.toJpaEntity(account);
        AccountJpaEntity saved = accountRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
