package com.atlas.bank.atlas.transaction.service.factory;

import com.atlas.bank.atlas.transaction.model.Transaction;
import com.atlas.bank.atlas.transaction.model.TransactionStatus;
import com.atlas.bank.atlas.transaction.model.TransactionType;
import com.atlas.bank.atlas.transaction.service.transfer.TransferContext;

import java.math.BigDecimal;

public class TransactionFactory {

    public static Transaction createTransfer(TransferContext context, BigDecimal fee) {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.TRANSFER);
        transaction.setSourceAccountId(context.from().getId());
        transaction.setTargetAccountId(context.to().getId());
        transaction.setAmount(context.amount());
        transaction.setFee(fee);
        transaction.setStatus(TransactionStatus.EXECUTED);

        return transaction;
    }
}
