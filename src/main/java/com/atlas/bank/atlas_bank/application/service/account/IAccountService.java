package com.atlas.bank.atlas_bank.application.service.account;

import com.atlas.bank.atlas_bank.domain.model.account.Account;

import java.util.List;

public interface IAccountService {

    List<Account> findAll();

    Account findById(Long id);

    Account create(Account account);
}
