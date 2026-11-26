package com.atlas.bank.atlas_bank.application.service.account;

import com.atlas.bank.atlas_bank.domain.model.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Primary
//ESta desactivado es un Cache de formq manual
@ConditionalOnProperty(name = "atlas.account.cache.enabled", havingValue = "true")
public class CachedAccountService implements IAccountService {

    private final IAccountService delegate;
    private final Map<Long, Account> cache = new ConcurrentHashMap<>();

    public CachedAccountService(@Qualifier("auditableAccountService") IAccountService delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<Account> findAll() {
        return List.of();
    }

    @Override
    public Account findById(Long id) {
        Account cached = cache.get(id);
        if (cached != null) {
            log.info("Cuenta {} encontrada en cache - NO se llama al service real", id);
            return cached;
        }
        log.info("Cuenta {} no esta en cache - delegando al service real", id);
        Account account = delegate.findById(id);
        cache.put(id, account);
        return account;
    }

    @Override
    public Account create(Account account) {
        Account created = delegate.create(account);
        cache.put(created.getId(), created);
        log.info("Cuenta {} agregada al cache", created.getId());
        return created;
    }
}
