package com.atlas.bank.atlas.domain.validation;

import com.atlas.bank.atlas.application.port.out.FraudCheckPort;
import com.atlas.bank.atlas.domain.exepcion.FraudCheckException;
import com.atlas.bank.atlas.domain.model.shared.FraudCheckResult;
import com.atlas.bank.atlas.domain.model.transaction.TransferContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
@RequiredArgsConstructor
public class FraudValidator implements TransferValidator {
    private final FraudCheckPort fraudChecker;

    @Override
    public void validate(TransferContext context) {
        FraudCheckResult fraudCheckResult = fraudChecker.check(context.from().getId(), context.amount());
        if (fraudCheckResult.blocked()) {
            throw new FraudCheckException(fraudCheckResult.reason());
        }
    }
}
