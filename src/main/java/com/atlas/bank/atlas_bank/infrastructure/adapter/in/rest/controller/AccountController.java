package com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.controller;

import com.atlas.bank.atlas_bank.application.service.account.IAccountService;
import com.atlas.bank.atlas_bank.domain.model.account.Account;
import com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.dto.request.CreateAccountRequest;
import com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.dto.response.AccountResponse;
import com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.dto.response.DashboardResponse;
import com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.facade.AccountDashboardFacade;
import com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.mapper.AccountMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService accountService;
    private final AccountMapper accountMapper;
    private final AccountDashboardFacade dashboardFacade;

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        final List<AccountResponse> responseList = accountService.findAll().stream()
                .map(accountMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> findById(@PathVariable Long id) {
        final Account account = accountService.findById(id);
        return ResponseEntity.ok(accountMapper.toResponse(account));
    }

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long id) {
        return ResponseEntity.ok(dashboardFacade.getDashboard(id));
    }
    
    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        final Account account = accountService.create(accountMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(accountMapper.toResponse(account));
    }
}
