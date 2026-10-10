package com.atlas.bank.atlas.domain.validation;

import com.atlas.bank.atlas.domain.model.account.AccountStatus;
import com.atlas.bank.atlas.domain.exepcion.AccountNotActiveException;
import com.atlas.bank.atlas.domain.model.transaction.TransferContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class StatusAccountValidator implements TransferValidator {
    @Override
    public void validate(TransferContext context) {
        if (context.from().getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(context.from().getId(), context.from().getStatus().name());
        }
        if (context.to().getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(context.to().getId(), context.to().getStatus().name());
        }
    }
}
