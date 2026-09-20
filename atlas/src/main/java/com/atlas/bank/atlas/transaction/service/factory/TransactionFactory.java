package com.atlas.bank.atlas.transaction.service.factory;

import com.atlas.bank.atlas.transaction.model.Transaction;
import com.atlas.bank.atlas.transaction.service.transfer.TransferContext;

import java.math.BigDecimal;

public class TransactionFactory {

    public static Transaction createTransfer(TransferContext context, BigDecimal fee) {
        Transaction transaction = new Transaction();
        transaction.setType("TRANSFER");
        transaction.setSourceAccountId(context.from().getId());
        transaction.setTargetAccountId(context.to().getId());
        transaction.setAmount(context.amount());
        transaction.setFee(fee);
        transaction.setStatus("EXECUTED");

        return transaction;
    }
}
