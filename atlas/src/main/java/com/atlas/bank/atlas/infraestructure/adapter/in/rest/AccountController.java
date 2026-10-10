package com.atlas.bank.atlas.infraestructure.adapter.in.rest;

import com.atlas.bank.atlas.infraestructure.adapter.in.rest.dto.AccountMapper;
import com.atlas.bank.atlas.infraestructure.adapter.in.rest.dto.AccountResponse;
import com.atlas.bank.atlas.infraestructure.adapter.in.rest.dto.CreateAccountRequest;
import com.atlas.bank.atlas.infraestructure.adapter.in.rest.dto.DashboardResponse;

import com.atlas.bank.atlas.account.service.AccountDashboardFacade;
import com.atlas.bank.atlas.application.service.IAccountService;
import com.atlas.bank.atlas.domain.model.account.Account;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@AllArgsConstructor
public class AccountController {

    private final IAccountService accountService;
    private final AccountMapper accountMapper;
    private final AccountDashboardFacade dashboardFacade;

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long id) {
        return ResponseEntity.ok(dashboardFacade.getDashboard(id));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        Account account = this.accountMapper.toEntity(request);

        Account accountCreated = accountService.create(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.accountMapper.toResponse(accountCreated));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        List<AccountResponse> accountResponses = accountService.findAll()
                .stream()
                .map(this.accountMapper::toResponse)
                .toList();

        return ResponseEntity.ok(accountResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(this.accountMapper.toResponse(this.accountService.findById(id)));
    }
}
