package com.atlas.bank.atlas.account.service;

import com.atlas.bank.atlas.account.dto.DashboardResponse;
import com.atlas.bank.atlas.account.model.Account;
import com.atlas.bank.atlas.transaction.dto.TransactionMapper;
import com.atlas.bank.atlas.transaction.dto.TransactionResponse;
import com.atlas.bank.atlas.transaction.service.ITransactionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountDashboardFacade {
    private final IAccountService accountService;
    private final ITransactionQueryService transactionQueryService;
    private final TransactionMapper transactionMapper;

    public DashboardResponse getDashboard(Long accountId) {
        Account account = this.accountService.findById(accountId);
        // Lista de trx
        List<TransactionResponse> transactions = transactionQueryService
                .getByAccountId(accountId)
                .stream()
                .map(transactionMapper::toResponse)
                .toList();

        return DashboardResponse.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .ownerName(account.getOwnerName())
                .type(account.getType().name())
                .balance(account.getBalance())
                .status(account.getStatus().name())
                .recentTransactions(transactions)
                .build();
    }
}
