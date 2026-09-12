package com.atlas.bank.atlas.account.controller;

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

    @PostMapping
    public ResponseEntity<AccountResponse> create(@RequestBody CreateAccountRequest request) {
        Account account = new Account();
        account.setAccountNumber(request.getAccountNumber());
        account.setOwnerName(request.getOwnerName());
        account.setEmail(request.getEmail());
        account.setType(request.getType());
        account.setBalance(request.getBalance());

        Account accountCreated = accountService.create(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(accountCreated));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        List<AccountResponse> accountResponses = accountService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(accountResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(accountService.findById(id)));
    }

    private AccountResponse toResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setOwnerName(account.getOwnerName());
        response.setEmail(account.getEmail());
        response.setType(account.getType());
        response.setBalance(account.getBalance());
        response.setStatus(account.getStatus());
        response.setCreatedAt(account.getCreatedAt());

        return response;
    }
}
