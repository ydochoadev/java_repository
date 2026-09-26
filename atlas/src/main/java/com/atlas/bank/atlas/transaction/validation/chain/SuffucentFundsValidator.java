package com.atlas.bank.atlas.transaction.validation.chain;

import com.atlas.bank.atlas.transaction.exeption.InsufficientFundsException;
import com.atlas.bank.atlas.transaction.service.transfer.TransferContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class SuffucentFundsValidator implements TransferValidator {
    @Override
    public void validate(TransferContext context) {
        if (context.from().getBalance().compareTo(context.amount()) < 0) {
            throw new InsufficientFundsException(context.from().getId(), context.from().getBalance(), context.amount());
        }
    }
}
