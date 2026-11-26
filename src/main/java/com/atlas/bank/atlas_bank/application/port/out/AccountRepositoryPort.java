package com.atlas.bank.atlas_bank.application.port.out;

import com.atlas.bank.atlas_bank.domain.model.account.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepositoryPort {
	
	List<Account> findAll();

    Optional<Account> findById(Long id);

    Account save(Account account);
}
