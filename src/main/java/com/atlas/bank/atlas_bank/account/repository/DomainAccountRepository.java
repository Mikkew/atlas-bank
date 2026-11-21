package com.atlas.bank.atlas_bank.account.repository;

import com.atlas.bank.atlas_bank.account.model.Account;

import java.util.List;
import java.util.Optional;

public interface DomainAccountRepository {

    List<Account> findAll();

    Optional<Account> findById(Long id);

    Account save(Account account);
}
