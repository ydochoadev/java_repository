package com.atlas.bank.atlas.transaction.controller;

import com.atlas.bank.atlas.transaction.dto.TransferRequest;
import com.atlas.bank.atlas.transaction.dto.TransactionResponse;
import com.atlas.bank.atlas.transaction.model.Transaction;
import com.atlas.bank.atlas.transaction.service.ITransactionQueryService;
import com.atlas.bank.atlas.transaction.service.ITransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransferService transferService;
    private final ITransactionQueryService transactionQueryService;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody TransferRequest request) {
        Transaction transaction = transferService.execute(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount()
        );

        return ResponseEntity.ok(toResponse(transaction));

    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable Long id) {
        List<TransactionResponse> transactionResponseList = transactionQueryService.getByAccountId(id)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(transactionResponseList);
    }

    TransactionResponse toResponse(Transaction t) {
        TransactionResponse response = new TransactionResponse();
        response.setId(t.getId());
        response.setType(t.getType());
        response.setSourceAccountId(t.getSourceAccountId());
        response.setTargetAccountId(t.getTargetAccountId());
        response.setAmount(t.getAmount());
        response.setFee(t.getFee());
        response.setStatus(t.getStatus());
        response.setCreatedAt(t.getCreatedAt());

        return response;
    }
}
