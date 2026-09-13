package com.atlas.bank.atlas.transaction.validation;

import com.atlas.bank.atlas.transaction.dto.TransferRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DifferentAccountValidator implements ConstraintValidator<DifferentAccount, TransferRequest> {

    @Override
    public boolean isValid(TransferRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if (request.getFromAccountId() == null || request.getToAccountId() == null) {
            return true;
        }
        // Solo validar las cuentas
        return !request.getFromAccountId().equals(request.getToAccountId());
    }
}
