package com.atlas.bank.atlas.account.controller;

import com.atlas.bank.atlas.account.dto.AccountMapper;
import com.atlas.bank.atlas.account.dto.AccountResponse;
import com.atlas.bank.atlas.account.dto.CreateAccountRequest;
import com.atlas.bank.atlas.account.model.Account;
import com.atlas.bank.atlas.account.service.IAccountService;
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

    @PostMapping
    public ResponseEntity<AccountResponse> create(@RequestBody CreateAccountRequest request) {
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
