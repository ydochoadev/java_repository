package com.atlas.bank.atlas.transaction.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    private String type; // DEPOSIT, WITHDRAWAL, TRANSFER
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
}
