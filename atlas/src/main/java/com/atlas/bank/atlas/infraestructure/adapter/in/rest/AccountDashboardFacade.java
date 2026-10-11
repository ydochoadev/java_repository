package com.atlas.bank.atlas.infraestructure.adapter.in.rest;

import com.atlas.bank.atlas.application.port.in.GetAccountUseCase;
import com.atlas.bank.atlas.application.port.in.GetTransactionsByAccountUseCase;
import com.atlas.bank.atlas.infraestructure.adapter.in.rest.dto.DashboardResponse;
import com.atlas.bank.atlas.domain.model.account.Account;
import com.atlas.bank.atlas.infraestructure.adapter.in.rest.dto.TransactionMapper;
import com.atlas.bank.atlas.infraestructure.adapter.in.rest.dto.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountDashboardFacade {
    private final GetAccountUseCase getAccountUseCase;
    private final GetTransactionsByAccountUseCase getTransactionsByAccountUseCase;
    private final TransactionMapper transactionMapper;

    public DashboardResponse getDashboard(Long accountId) {
        Account account = this.getAccountUseCase.findById(accountId);
        // Lista de trx
        List<TransactionResponse> transactions = getTransactionsByAccountUseCase
                .getByAccountId(accountId)
                .stream()
                .map(transactionMapper::toResponse)
                .toList();

        return DashboardResponse.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .ownerName(account.getOwnerName())
                .type(account.getType().name())
                .balance(account.getBalance().getAmount())
                .status(account.getStatus().name())
                .recentTransactions(transactions)
                .build();
    }
}
