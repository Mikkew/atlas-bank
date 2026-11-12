package com.atlas.bank.atlas_bank.service;

import com.atlas.bank.atlas_bank.model.Account;

import java.util.List;

public interface IAccountService {

    List<Account> findAll();

    Account findById(Long id);

    Account create(Account account);
}
